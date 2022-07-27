package space.mosk.tourismore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import space.mosk.tourismore.models.FeedPost
import java.util.*
import java.util.concurrent.Semaphore


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFragment : Fragment() {
    private lateinit var AllPosts: List<FeedPost?>
    private lateinit var posts: List<FeedPost?>

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var followsList: List<String>

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
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        val feed_recycler = view.findViewById<RecyclerView>(R.id.feed_recycler)


        mDatabase.child("users").child(mAuth.uid.toString()).child("follows")
            .addValueEventListener(ValueEventListenerAdapter{dataSnapshot ->
                followsList = listOf()
                followsList = followsList.plus(mAuth.uid.toString())
                for (snapshot in dataSnapshot.children){
                    followsList = followsList.plus(snapshot.key.toString())!!
                }
                val postsRef = mDatabase.child("feed-posts")
                postsRef.addValueEventListener(ValueEventListenerAdapter{dataSnapshot->
                    posts = listOf()
                    for (snapshot in dataSnapshot.children){
                        val post = snapshot.getValue(FeedPost::class.java)
                        for (id in followsList){
                            if (post!!.uid == id){
                                posts = posts + post
                            }
                        }
                    }
                    feed_recycler.adapter = FeedAdapter(posts.asReversed() as List<FeedPost>)
                    feed_recycler.layoutManager = LinearLayoutManager(view.context)
                    if (feed_recycler.adapter?.itemCount == 0){
                        view.findViewById<TextView>(R.id.emptyTextNews).visibility = View.VISIBLE
                    } else{
                        view.findViewById<TextView>(R.id.emptyTextNews).visibility = View.GONE
                    }
                })
            })
        return view
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}