package eu.peernetwork.app.model

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("POST") val post: Post,
    @SerializedName("COMMENT") val comment: Comment,
    @SerializedName("USER") val user: User,
    @SerializedName("CHAT") val chat: Chat,
    @SerializedName("CONTACT") val contact: Contact,
    @SerializedName("PAGING") val paging: Paging,
    @SerializedName("WALLET") val wallet: Wallet,
    @SerializedName("ONBOARDING") val onboarding: Onboarding,
    @SerializedName("DAILY_FREE") val dailyFree: DailyFree,
    @SerializedName("TOKENOMICS") val tokenomics: Tokenomics,
    @SerializedName("MINTING") val minting: Minting
) {
    data class Range(
        @SerializedName("MIN_LENGTH") val minLength: Int? = null,
        @SerializedName("MAX_LENGTH") val maxLength: Int? = null,
        @SerializedName("PATTERN") val pattern: String? = null,
        @SerializedName("MAX_COUNT") val maxCount: Int? = null,
        @SerializedName("MIN") val min: Long? = null,
        @SerializedName("MAX") val max: Double? = null,
        @SerializedName("LENGTH") val length: Int? = null,
        @SerializedName("MAX_SIZE_MB") val maxSizeMb: Int? = null,
        @SerializedName("MIN_TOKENS") val minTokens: Int? = null
    )

    data class Post(
        @SerializedName("TITLE") val title: Range,
        @SerializedName("MEDIADESCRIPTION") val mediaDescription: Range,
        @SerializedName("COVER") val cover: Range,
        @SerializedName("MEDIA") val media: Range,
        @SerializedName("OPTIONS") val options: Range,
        @SerializedName("MEDIALIMIT") val mediaLimit: MediaLimit,
        @SerializedName("COVERLIMIT") val coverLimit: MediaLimit,
        @SerializedName("TAG") val tag: Tag
    )

    data class MediaLimit(
        @SerializedName("AUDIO") val audio: Int,
        @SerializedName("IMAGE") val image: Int,
        @SerializedName("TEXT") val text: Int,
        @SerializedName("VIDEO") val video: Int
    )

    data class Tag(
        @SerializedName("MIN_LENGTH") val minLength: Int,
        @SerializedName("MAX_LENGTH") val maxLength: Int,
        @SerializedName("PATTERN") val pattern: String,
        @SerializedName("MAX_COUNT") val maxCount: TagCount
    )

    data class TagCount(
        @SerializedName("CREATE") val create: Int,
        @SerializedName("SEARCH") val search: Int
    )

    data class Comment(@SerializedName("CONTENT") val content: Range)

    data class User(
        @SerializedName("PASSWORD") val password: Range,
        @SerializedName("USERNAME") val username: Range,
        @SerializedName("BIOGRAPHY") val biography: Range,
        @SerializedName("PHONENUMBER") val phoneNumber: Range,
        @SerializedName("IMAGE") val image: Range,
        @SerializedName("SLUG") val slug: Range,
        @SerializedName("LIQUIDITY") val liquidity: Range,
        @SerializedName("AVATAR") val avatar: Range,
        @SerializedName("TRANSACTION") val transaction: Range
    )

    data class Chat(
        @SerializedName("MESSAGE") val message: Range,
        @SerializedName("NAME") val name: Range,
        @SerializedName("IMAGE") val image: Range,
        @SerializedName("IS_PUBLIC") val isPublic: Public,
        @SerializedName("ACCESS_LEVEL") val accessLevel: AccessLevel
    )

    data class Public(
        @SerializedName("MIN") val min: Int,
        @SerializedName("MAX") val max: Int,
        @SerializedName("SUSPENDED") val suspended: Int
    )

    data class AccessLevel(
        @SerializedName("MIN") val min: Int,
        @SerializedName("MAX") val max: Int,
        @SerializedName("USER") val user: Int,
        @SerializedName("ADMIN") val admin: Int
    )

    data class Contact(
        @SerializedName("NAME") val name: Range,
        @SerializedName("MESSAGE") val message: Range
    )

    data class Paging(
        @SerializedName("OFFSET") val offset: MinMax,
        @SerializedName("LIMIT") val limit: MinMax
    )

    data class MinMax(
        @SerializedName("MIN") val min: Long,
        @SerializedName("MAX") val max: Double
    )

    data class Wallet(
        @SerializedName("SOLANA_PUBKEY") val solanaPubKey: Range,
        @SerializedName("TOKEN") val token: Range,
        @SerializedName("NUMBERS") val numbers: MinMax,
        @SerializedName("NUMBERSQ") val numbersQ: MinMax,
        @SerializedName("WHEREBY") val whereby: MinMax
    )

    data class Onboarding(
        @SerializedName("AVAILABLE_ONBOARDINGS") val availableOnboardings: List<String>
    )

    data class DailyFree(
        @SerializedName("DAILY_FREE_ACTIONS") val dailyFreeActions: Map<String, Int>
    )

    data class Tokenomics(
        @SerializedName("ACTION_TOKEN_PRICES") val actionTokenPrices: Map<String, Int>,
        @SerializedName("ACTION_GEMS_RETURNS") val actionGemsReturns: Map<String, Double>
    )

    data class Minting(
        @SerializedName("DAILY_NUMBER_TOKEN") val dailyNumberToken: Int
    )
}
