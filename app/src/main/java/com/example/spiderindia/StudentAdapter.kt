package com.example.spiderindia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
class StudentAdapter(private val students: List<Student>, private val context: Context) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_data, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.studentName.text = student.name

        // Format the class value to "6th std"
        holder.studentClass.text = formatClass(student.studentClass)
        holder.studentSection.text=formatClass(student.studentSection)

        if (!student.imageUri.isNullOrEmpty()) {
            try {
                holder.studentImage.setImageURI(Uri.parse(student.imageUri))
            } catch (e: Exception) {
                e.printStackTrace()
                holder.studentImage.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            holder.studentImage.setImageResource(R.drawable.ic_launcher_background)
        }

        // Set click listener on forward arrow
        holder.forwardArrow.setOnClickListener {
            val intent = Intent(context, ProfileViewActivity::class.java).apply {
                putExtra("studentName", student.name)
                putExtra("studentClass", formatClass(student.studentClass))

                putExtra("imageUri", student.imageUri)
                putExtra("studentSection", student.studentSection)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = students.size

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.StudentName)
        val studentClass: TextView = itemView.findViewById(R.id.Class)
        val studentSection: TextView = itemView.findViewById(R.id.Section)
        val studentImage: ImageView = itemView.findViewById(R.id.studentImage)
        val forwardArrow: LinearLayout = itemView.findViewById(R.id.forwardArrow)
    }

    // Helper function to format class value
    private fun formatClass(classValue: String): String {
        return when (classValue) {
            "1" -> "1st std"
            "2" -> "2nd std"
            "3" -> "3rd std"
            "4" -> "4th std"
            "5" -> "5th std"
            "6" -> "6th std"
            "7" -> "7th std"
            "8" -> "8th std"
            "9" -> "9th std"
            "10" -> "10th std"
            "11" -> "11th std"
            "12" -> "12th std"
            else -> "$classValue std"
        }

    }
}
