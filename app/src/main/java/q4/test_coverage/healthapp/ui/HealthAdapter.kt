package q4.test_coverage.healthapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import q4.test_coverage.healthapp.R
import q4.test_coverage.healthapp.databinding.ItemRvBinding
import q4.test_coverage.healthapp.model.HealthData
import q4.test_coverage.healthapp.utils.ItemTouchHelperAdapter
import q4.test_coverage.healthapp.utils.ItemTouchHelperViewHolder

class HealthAdapter : RecyclerView.Adapter<HealthAdapter.HealthAdapterViewHolder>(),
    ItemTouchHelperAdapter {

    private var allHealthList: MutableList<HealthData> = arrayListOf()
    private val fireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = HealthAdapterViewHolder(
        ItemRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun getItemCount(): Int = allHealthList.size

    override fun onBindViewHolder(holder: HealthAdapterViewHolder, position: Int) {
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
        private val vb: ItemRvBinding
    ) :
        RecyclerView.ViewHolder(vb.root), ItemTouchHelperViewHolder {

        fun bind(data: HealthData) = with(vb) {
            vb.timeTw.text = data.date
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