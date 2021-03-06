package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.planning

import android.app.Activity
import android.content.Intent
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPeriodBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDaysCircleBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListTemplatesBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodState
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AutoScheduleActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.PlanWeekActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import okhttp3.internal.toHexString
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PeriodFragment : ViewModelFragment<PeriodViewModel, FragmentPeriodBinding>(
    R.layout.fragment_period,
    PeriodViewModel::class
) {
    override val toolbar: Toolbar get() = binding.planningToolbar.toolbar
    override var navigateUp = true

    private val sharedVM: SharedViewModel by sharedViewModel()

    private val args by navArgs<PeriodFragmentArgs>()

    private val periodObserver by lazy {
        Observer<SchedulingPeriod> {
            setupMenu()
            sharedVM.periodChanged.value = Consumable(true)
        }
    }

    private val successObserver by lazy {
        Observer<Boolean> {
            if (it) {
                sharedVM.periodChanged.value = Consumable(true)
                viewModel.fetchPeriod()
            }
        }
    }

    private val unitsObserver by lazy {
        Observer<List<SchedulingUnit>> {
            if (it != null) setupMenu()
        }
    }

    private val observer by lazy {
        Observer<List<Selection<PeriodDay>>> {
            binding.rvUnits.adapter = BaseListAdapter<Selection<PeriodDay>>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListDaysCircleBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { unit, binding, _ -> (binding as ListDaysCircleBinding).day = unit },
                { selection, _ -> selectUnit(selection) },
                { old, new -> old.item.id == new.item.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
            setupMenu()
        }
    }

    private val shiftsObserver by lazy {
        Observer<List<ShiftTemplate>> {
            binding.rvShifts.adapter = BaseListAdapter<ShiftTemplate>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListTemplatesBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { template, binding, _ ->
                    (binding as ListTemplatesBinding).template = template
                },
                { template, _ ->
                    findNavController().navigate(
                        PeriodFragmentDirections.navigateToTemplateFragment(
                            template,
                            args.period
                        )
                    )
                    Logger.d(template)
                },
                { old, new -> old.id == new.id },
                { old, new -> old == new },
            ).apply {
                setItems(it)
            }
        }
    }

    override fun initUi() {
        viewModel.daySelection.observe(viewLifecycleOwner, observer)
        viewModel.templates.observe(viewLifecycleOwner, shiftsObserver)

        viewModel.success.observe(viewLifecycleOwner, successObserver)
        viewModel.units.observe(viewLifecycleOwner, unitsObserver)

        viewModel.setPeriod(args.period)
        setupMenu()

        viewModel.period.observe(viewLifecycleOwner, periodObserver)

        binding.emptyWeekMessage.btnPlanShift.setOnClickListener {
            startActivityForResult<PlanWeekActivity>(PLAN_PERIOD_RC) {
                this.apply {
                    putSerializable(PlanWeekActivity.SCHEDULING_PERIOD, viewModel.period.value)
                }
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (viewModel.period.value?.state == PeriodState.CURRENT ||
                viewModel.period.value?.state == PeriodState.TO_BE_SUBMITTED
            ) {
                showSubmitDialog()
            } else if (viewModel.period.value?.state == PeriodState.TO_BE_PLANNED) {
                startAutoSchedule()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            viewModel.fetchSchedulingUnits()
            sharedVM.periodChanged.value = Consumable(true)
            viewModel.fetchPeriod()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupMenu() {
        Logger.d("Setup menu ${viewModel.units.value} / ${viewModel.period.value?.state}")
        if (toolbar.menu.isEmpty()) toolbar.inflateMenu(R.menu.period_schedule)

        toolbar.menu.forEach {
            val color = resources.getColor(R.color.text, null).toHexString()
            Logger.d(color)
            it.title = Html.fromHtml(
                "<font color='#${
                    color.substring(
                        2,
                        color.length
                    )
                }'>${it.title}</font>", 0
            )
        }

        if (
            viewModel.period.value?.state == PeriodState.SUBMITTED ||
            viewModel.period.value?.state == PeriodState.CURRENT ||
            (viewModel.period.value?.state == PeriodState.TO_BE_PLANNED && viewModel.units.value.isNullOrEmpty())
        ) {
            toolbar.menu.forEach {
                it.isVisible = false
            }
        } else if (viewModel.period.value?.state == PeriodState.TO_BE_PLANNED) {
            toolbar.menu.forEach {
                it.isVisible = true
            }
        }

        if (viewModel.period.value?.state == PeriodState.TO_BE_SUBMITTED) {
            toolbar.menu.forEach {
                it.isVisible = true
                it.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
            }
        }

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_auto) {
                startAutoSchedule()
            } else if (it.itemId == R.id.action_edit) {
                startActivityForResult<PlanWeekActivity>(PLAN_PERIOD_RC) {
                    this.apply {
                        putInt(PlanWeekActivity.MODE, PlanWeekActivity.MODE_UPDATE_DEMAND)
                        putSerializable(PlanWeekActivity.SCHEDULING_PERIOD, viewModel.period.value)
                    }
                }
            } else if (it.itemId == R.id.action_submit) {
                showSubmitDialog()
            }
            true
        }
    }

    private fun selectUnit(day: Selection<PeriodDay>) {
        viewModel.daySelection.value?.select(day) { newIndex, originalIndex ->
            binding.rvUnits.adapter?.apply {
                notifyItemChanged(newIndex)
                notifyItemChanged(originalIndex)
            }
            viewModel.fetchShiftsInUnit(day.item.id)
        }
    }

    private fun startAutoSchedule() {
        startActivityForResult<AutoScheduleActivity>(AUTO_SCHEDULE_RC) {
            this.apply {
                putSerializable(
                    AutoScheduleActivity.SCHEDULING_PERIOD,
                    viewModel.period.value
                )
            }
        }
    }

    private fun showSubmitDialog() {
        showMaterialDialog(
            R.string.are_you_sure_you_want_to_submit,
            positive = R.string.yes,
            negative = R.string.no,
            onPositive = { viewModel.submit() },
            onNegative = { it.dismiss() }
        )
    }

    companion object {
        private const val PLAN_PERIOD_RC = 1112
        private const val AUTO_SCHEDULE_RC = 1113
    }
}
