package space.mosk.tourismore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flaviofaria.kenburnsview.KenBurnsView

class TopPlacesAdapter(val array : List<Places>) : RecyclerView.Adapter<TopPlacesAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.tourTitle)
        val descr : TextView = itemView.findViewById(R.id.tourDescription)
        val img : KenBurnsView = itemView.findViewById(R.id.tourImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.top_place, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = array[position].title
        holder.descr.text = array[position].description
        Glide.with(holder.itemView.context).load(array[position].urlToImg).into(holder.img)
    }

    override fun getItemCount(): Int {
        return array.size
    }

}

data class Places(val title : String, val description : String, val urlToImg : String)

fun makePlaces() : List<Places>{
    return listOf(
        Places("Красная площадь", "Красная площадь по праву " +
                "возглавляет рейтинг самых популярных мест в Москве. " +
                "Ее выверенный до сантиметра, веками формировавшийся " +
                "архитектурный ансамбль поражает воображение своей монументальностью и благородным величием.", "https://незабываемая.москва/blog/samyye_poseshchayemyye_mesta_v_moskve_1_IS.jpg"),
        Places("ГУМ", "Самый известный магазин страны – уникальный историко-культурный " +
                "памятник мирового уровня и полноценная визитная карточка города.", "https://незабываемая.москва/blog/samyye_poseshchayemyye_mesta_v_moskve_2_IS.jpg"),
        Places("Музеи Московского Кремля", "Культурно-исторический музей-заповедник «Московский Кремль» (ММК) фактически был основан Александром I " +
                "в 1806 году на базе Оружейной палаты.", "https://незабываемая.москва/blog/samyye_poseshchayemyye_mesta_v_moskve_3_IS.jpg")
    )
}