import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = StringDescriptor

    // Consider wrapping in ThreadLocal if serialization may happen in multiple threads
    private val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(df.format(obj))
    }

    override fun deserialize(input: Decoder): Date {
        return df.parse(input.decodeString())
    }
}