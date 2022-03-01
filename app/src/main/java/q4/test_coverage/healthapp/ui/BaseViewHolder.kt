package q4.test_coverage.healthapp.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import q4.test_coverage.healthapp.model.HealthData

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: HealthData)

}