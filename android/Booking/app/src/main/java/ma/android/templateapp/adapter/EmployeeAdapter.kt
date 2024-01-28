package ma.android.templateapp.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_view_employee.view.*
import ma.android.templateapp.R
import ma.android.templateapp.model.Entity
import ma.android.templateapp.networking.RestApi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

@RequiresApi(Build.VERSION_CODES.M)
class EmployeeAdapter(val context: Context): RecyclerView.Adapter<EmployeeAdapter.EntityViewAdapter>(){

    private val employee by lazy { RestApi.create() }
    private var entitiesList: ArrayList<Entity> = ArrayList()

    init { refreshEntities() }

    class EntityViewAdapter(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntityViewAdapter {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_employee, parent, false)

        return EntityViewAdapter(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: EntityViewAdapter, position: Int) {

        holder.view.idViewEmployee.text = entitiesList[position].id.toString()
        holder.view.nameViewEmployee.text = entitiesList[position].name
        holder.view.doctorViewEmployee.text = entitiesList[position].doctor
        holder.view.dateViewEmployee.text = entitiesList[position].hour.toString()
        holder.view.hourViewEmployee.text = entitiesList[position].date.toString()

        holder.view.confirmButton.setOnClickListener {
            showConfirmDialog(holder, entitiesList[position])
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showConfirmDialog(holder: EntityViewAdapter, entity: Entity) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Confirm")
        dialogBuilder.setMessage("Confirm booking?")
        dialogBuilder.setPositiveButton("Confirm") { _, _ ->
            confirmEntity(entity)
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    private fun confirmEntity(entity: Entity) {
        if (checkOnline()) {
            entitiesList[entity.id].status = true
            entity.status = true
            refreshEntities()
            Log.d("Entity confirmed -> ", entity.toString())

            /*employee.confirmEntity(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        entitiesList[id].status = true
                        refreshEntities()
                        Log.d("Entity confirmed -> ", id.toString())
                    },
                    { throwable ->
                        Toast.makeText(context, "Confirm error: ${throwable.message}", Toast.LENGTH_LONG).show()
                    }
                )*/
        }
        else {
            Toast.makeText(context, "Not online!", Toast.LENGTH_LONG).show()
        }

    }
    override fun getItemCount() = entitiesList.size

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun refreshEntities() {
        if (checkOnline()) {
            employee.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        entitiesList.clear()
                        entitiesList.addAll(result)
                        entitiesList.sortWith(compareBy({ it.id }, { it.name }))
                        notifyDataSetChanged()
                        Log.d("Entities -> ", entitiesList.toString())
                    },
                    { throwable ->
                        if (throwable is HttpException) {
                            val body: ResponseBody = throwable.response()?.errorBody()!!
                            Toast.makeText(
                                context,
                                "Error: ${JSONObject(body.string()).getString("text")}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
        }
        else {
            Toast.makeText(context, "Not online!", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    }
}