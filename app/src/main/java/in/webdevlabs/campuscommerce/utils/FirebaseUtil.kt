package `in`.webdevlabs.campuscommerce.utils

import `in`.webdevlabs.campuscommerce.model.Chat
import `in`.webdevlabs.campuscommerce.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


object FirebaseUtil {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    //private var pids = ArrayList<String>()

    fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun addCurrentUserToFirebaseDatabase() {
        val databaseRef: DatabaseReference = database.getReference("users")

        databaseRef.child(firebaseAuth.currentUser?.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.run {
                    if (!dataSnapshot.exists()) {
                        val userRef = databaseRef.child(firebaseAuth.currentUser?.uid)
                        userRef.child("name").setValue(firebaseAuth.currentUser?.displayName)
                        userRef.child("email").setValue(firebaseAuth.currentUser?.email)
                    }
                }
            }
        })
    }

    fun addPostToFirebaseDatabase(post: Post) {
        val postRef: DatabaseReference = database.getReference("posts").push()
        val key = postRef.key
        //pids.add(key)
        postRef.child("pid").setValue(key)
        postRef.child("name").setValue(post.name)
        postRef.child("price").setValue(post.price)
        postRef.child("uid").setValue(firebaseAuth.currentUser?.uid)
        postRef.child("time").setValue(post.time)
        postRef.child("type").setValue(post.type)
        postRef.child("pid").setValue(key)
        postRef.child("tag").setValue(post.tag)
    }

    fun addGroupToDatabase(suid: String, ruid: String, pid: String) {
        val gid = (suid.hashCode() + ruid.hashCode() + pid.hashCode()).toString()
        val groupRef: DatabaseReference = database.getReference("groups").child(gid)
        groupRef.child("gid").setValue(gid)
        groupRef.child("suid").setValue(suid)
        groupRef.child("ruid").setValue(ruid)
        groupRef.child("pid").setValue(pid)
//        groupRef.child("sname").setValue(sname)
    }

    fun addChatToFirebaseDatabase(gid: String, chat: Chat) {
        val chatRef: DatabaseReference = database.getReference("groups").child(gid).child("chats").push()
        chatRef.child("msg").setValue(chat.msg)
        chatRef.child("time").setValue(chat.time)
    }

    fun addTagToFirebaseDatabase(tag: String, uid: String) {
        val tagRef: DatabaseReference = database.getReference("users")
        tagRef.child(uid).child("tags").setValue(tag)
    }
}