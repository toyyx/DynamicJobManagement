import com.example.dynamicjobmanagement.model.model.Student
import com.example.dynamicjobmanagement.model.model.Teacher
import com.example.dynamicjobmanagement.model.model.User
import com.google.gson.InstanceCreator
import java.lang.reflect.Type
import com.google.gson.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class StudentInstanceCreator : InstanceCreator<Student> {
    override fun createInstance(type: Type?): Student {
        return Student()
    }
}

class TeacherInstanceCreator : InstanceCreator<Teacher> {
    override fun createInstance(type: Type?): Teacher {
        return Teacher()
    }
}
class userInstanceCreator : InstanceCreator<User> {
    override fun createInstance(type: Type?): User {
        return User()
    }
}

// 自定义的 LocalDateTime 类型适配器，用于处理 LocalDateTime 对象的序列化和反序列化
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    // 定义 LocalDateTime 的格式化器
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // 实现 JsonSerializer 接口的 serialize 方法，用于将 LocalDateTime 对象序列化为 JsonElement
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src!!.format(formatter))
    }

    // 实现 JsonDeserializer 接口的 deserialize 方法，用于将 JsonElement 反序列化为 LocalDateTime 对象
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(json!!.asString, formatter)
    }
}
