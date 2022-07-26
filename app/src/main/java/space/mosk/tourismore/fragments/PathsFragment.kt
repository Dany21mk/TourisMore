package space.mosk.tourismore.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.mosk.tourismore.*
import space.mosk.tourismore.adapters.ShareBetweenFragments


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PathsFragment : Fragment(), OnViewClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private val ways = makeSampleWays()

    private lateinit var listView: RecyclerView
    private lateinit var model : ShareBetweenFragments
    private lateinit var editText : EditText
    private lateinit var errorText : TextView
    private var currentArray : List<Way> = ways

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.fragment_paths, container, false)
        v.findViewById<Button>(R.id.make_path).setOnClickListener {
            val nextFrag = MakeRouteActivity()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, nextFrag, "")
                .addToBackStack(null)
                .commit()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.recyclerContainer)
        editText = view.findViewById(R.id.autoComplete)
        errorText = view.findViewById(R.id.errorText)
        editText.setTextColor(Color.parseColor("#ffffff"))

        listView.layoutManager = LinearLayoutManager(context)

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.devider)
            ?.let { itemDecorator.setDrawable(it) }

        listView.addItemDecoration(itemDecorator)
        listView.adapter = ListPathsAdapter(ways, this)

        editText.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var matchNames : List<Way> = listOf()
                for(i in 0 until ways.size){
                    if(ways[i].name.toLowerCase().contains(p0.toString().toLowerCase())){
                        matchNames += ways[i]
                    }
                }
                if(p0.toString() != ""){
                    currentArray = matchNames
                    listView.adapter = ListPathsAdapter(matchNames, this@PathsFragment)
                    if(matchNames.size == 0){
                        errorText.text = "Ничего не найдено..."
                    }else{
                        errorText.text = ""
                    }
                }
                else{
                    currentArray = ways
                    listView.adapter = ListPathsAdapter(ways, this@PathsFragment)
                    errorText.text = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PathsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(pos: Int) {
        model = ViewModelProvider(requireActivity()).get(ShareBetweenFragments::class.java)
        model.sendIndex(ways.indexOf(currentArray[pos]))
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_left, R.animator.slide_right)
            .replace(R.id.container, ChooseMapFragment())
            .commit()
    }

}