package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.util.SelectionList
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleSelectionList
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.deepCopy
import cz.cvut.fel.kopecm26.bakaplanner.util.selectionListOf

class AdjustShiftsViewModel : BaseViewModel() {

    private val _daysMap =
        MutableLiveData<Map<PeriodDay, SelectionList<Selection<ShiftTimeCalculation>>>>()
    val daysMap: LiveData<Map<PeriodDay, SelectionList<Selection<ShiftTimeCalculation>>>> = _daysMap

    private val _daySelection = MutableLiveData<SingleSelectionList<Selection<PeriodDay>>>()
    val daySelection: LiveData<SingleSelectionList<Selection<PeriodDay>>> = _daySelection

    val selectedDay: Selection<PeriodDay>? get() = _daySelection.value?.selected

    fun mapDays(
        days: List<PeriodDay>,
        times: List<ShiftTimeCalculation>,
        selectFirst: Boolean = true
    ) {
        val map = HashMap<PeriodDay, SelectionList<Selection<ShiftTimeCalculation>>>()
        val selections = SingleSelectionList<Selection<PeriodDay>>()

        days.forEach {
            map[it] = selectionListOf(times.deepCopy(), true) { _ -> it.date }
            selections.add(Selection(it))
        }

        if (selectFirst) selections[0].selected = true
        _daySelection.value = selections
        _daysMap.value = map
    }

    fun mapExcludedToMap(): Map<Int, ArrayList<Int>> {
        val days = _daysMap.value?.filter {
            !it.value.isAnySelected()
        }

        return HashMap<Int, ArrayList<Int>>().apply {
            days?.forEach {
                put(
                    it.key.id,
                    it.value.getAllUnselected().map { selection -> selection.item.id } as ArrayList<Int>
                )
            }
        }
    }
}