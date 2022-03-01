package q4.test_coverage.healthapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import q4.test_coverage.healthapp.R
import q4.test_coverage.healthapp.databinding.ItemRvGreenBinding
import q4.test_coverage.healthapp.databinding.ItemRvHeaderBinding
import q4.test_coverage.healthapp.databinding.ItemRvRedBinding
import q4.test_coverage.healthapp.databinding.ItemRvYellowBinding
import q4.test_coverage.healthapp.model.HealthData
import q4.test_coverage.healthapp.utils.ItemTouchHelperAdapter
import q4.test_coverage.healthapp.utils.ItemTouchHelperViewHolder

class HealthAdapter : RecyclerView.Adapter<BaseViewHolder>(),
    ItemTouchHelperAdapter {

    companion object {
        private const val TYPE_ONE = 1
        private const val TYPE_TWO = 2
        private const val TYPE_THREE = 3
        private const val TYPE_FOUR = 4
    }

    private var allHealthList: MutableList<HealthData> = arrayListOf()
    private val fireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return when (viewType) {
            TYPE_ONE -> HealthAdapterViewHolder(
                ItemRvGreenBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
            TYPE_TWO -> HealthAdapterViewHolderYellow(
                ItemRvYellowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
            TYPE_THREE -> HealthAdapterViewHolderRed(
                ItemRvRedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
            TYPE_FOUR -> HealthAdapterViewHolderHeader(
                ItemRvHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
            else -> HealthAdapterViewHolder(
                ItemRvGreenBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        }

    }

    override fun getItemCount(): Int = allHealthList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            allHealthList[position].type == HealthData.HEADER.HEADER -> TYPE_FOUR
            allHealthList[position].pulse?.toInt()!! in 51..90 -> TYPE_ONE
            allHealthList[position].pulse?.toInt()!! in 91..120 -> TYPE_TWO
            allHealthList[position].pulse?.toInt()!! > 120 -> TYPE_THREE
            allHealthList[position].pulse?.toInt()!! in 0..50 -> TYPE_THREE
            else -> TYPE_ONE
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(allHealthList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAllHealthData(requests: MutableList<HealthData>) {
        this.allHealthList = requests
        notifyDataSetChanged()
    }

    fun appendItem(request: HealthData) {
        allHealthList.add(request)
        notifyItemInserted(itemCount - 1)
    }

    inner class HealthAdapterViewHolder(
        private val vb: ItemRvGreenBinding
    ) :
        BaseViewHolder(vb.root), ItemTouchHelperViewHolder {

        override fun bind(data: HealthData) = with(vb) {
            vb.timeTw.text = data.time
            vb.pressureFirst.text = data.pressure_first
            vb.pressureSecond.text = data.pressure_second
            vb.pulseTw.text = data.pulse
        }

        override fun onItemSelected() {
            itemView.setBackgroundResource(R.drawable.item_recycler_background_when_removing)
        }

        override fun onItemClear() {
            itemView.setBackgroundResource(R.drawable.item_recycler_background)
        }
    }

    inner class HealthAdapterViewHolderYellow(
        private val vb: ItemRvYellowBinding
    ) :
        BaseViewHolder(vb.root), ItemTouchHelperViewHolder {

        override fun bind(data: HealthData) = with(vb) {
            vb.timeTw.text = data.time
            vb.pressureFirst.text = data.pressure_first
            vb.pressureSecond.text = data.pressure_second
            vb.pulseTw.text = data.pulse
        }

        override fun onItemSelected() {
            itemView.setBackgroundResource(R.drawable.item_recycler_background_when_removing)
        }

        override fun onItemClear() {
            itemView.setBackgroundResource(R.drawable.item_recycler_background)
        }
    }

    inner class HealthAdapterViewHolderRed(
        private val vb: ItemRvRedBinding
    ) :
        BaseViewHolder(vb.root), ItemTouchHelperViewHolder {

        override fun bind(data: HealthData) = with(vb) {
            vb.timeTw.text = data.time
            vb.pressureFirst.text = data.pressure_first
            vb.pressureSecond.text = data.pressure_second
            vb.pulseTw.text = data.pulse
        }

        override fun onItemSelected() {
            itemView.setBackgroundResource(R.drawable.item_recycler_background_when_removing)
        }

        override fun onItemClear() {
            itemView.setBackgroundResource(R.drawable.item_recycler_background)
        }
    }

    inner class HealthAdapterViewHolderHeader(
        private val vb: ItemRvHeaderBinding
    ) :
        BaseViewHolder(vb.root), ItemTouchHelperViewHolder {

        override fun bind(data: HealthData) {
            vb.dateTw.text = data.date
        }

        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    override fun onItemDismiss(position: Int) {

        val updates = hashMapOf<String, Any>(
            "healthDataList" to FieldValue.arrayRemove(allHealthList[position])
        )

        val docRef = fireStore.collection("health_data")
            .document("myUid")
        docRef.update(updates)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    allHealthList.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    return@addOnCompleteListener
                }
            }
    }
}
