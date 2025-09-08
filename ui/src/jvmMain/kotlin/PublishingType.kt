import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PublishingType {
    @SerialName("automatic") AUTOMATIC,
    @SerialName("user_managed") USER_MANAGED,
    @SerialName("portal_api") PORTAL_API;
}