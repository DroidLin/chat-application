import com.application.channel.core.Listener

class SimpleWriteResultListener : Listener {
    override fun onSuccess() {
        println("send message success.")
    }

    override fun onFailure(cause: Throwable) {
        println("failure sending message.")
    }
}