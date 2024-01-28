package ma.android.templateapp.adapter

import android.annotation.SuppressLint
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
class ClientAdapter(val context: Context): RecyclerView.Adapter<ClientAdapter.EntityViewAdapter>() {

    private val client by lazy { RestApi.create() }
    private var entitiesList: ArrayList<Entity> = ArrayList()

    init { refreshEntities() }

    class EntityViewAdapter(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntityViewAdapter {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_client, parent, false)

        return EntityViewAdapter(view)
    }

    override fun onBindViewHolder(holder: EntityViewAdapter, position: Int) {
        holder.view.doctorViewEmployee.text = entitiesList[position].doctor
        holder.view.nameViewEmployee.text = entitiesList[position].name
        holder.view.dateViewEmployee.text = entitiesList[position].hour.toString()
    }

    override fun getItemCount() = entitiesList.size

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun refreshEntities() {
        if (checkOnline()) {
            client.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        entitiesList.clear()
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
    @SuppressLint("CheckResult")
    fun addEntity(element: Entity) {
        if (checkOnline()) {
            client.addEntity(element)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        refreshEntities()
                        Log.d("Entity added -> ", element.toString())
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
    fun checkOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    }
}