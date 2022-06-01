import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("username") username:String,
        @Field("password") password: String
    ): Call<LoginResponses>

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("username") username:String,
        @Field("password") password: String
    ): Call<RegisterResponses>

    @GET("balance")
    fun getBalance(
        @Header("Authorization") auth: String
    ): Call<Balance>

    @GET("payees")
    fun getPayees(
        @Header("Authorization") auth: String
    ): Call<Payees>

    @GET("transactions")
    fun getTransactions(
        @Header("Authorization") auth: String
    ): Call<Transactions>

    @FormUrlEncoded
    @POST("transfer")
    fun postTransfer(
        @Header("Authorization") auth: String,
        @Field("receipientAccountNo") receipientAccountNo: String,
        @Field("amount") amount: Double,
        @Field("description") description: String
    ): Call<TransferResponses>

}

object ApiHelper {
    val baseURL = "https://green-thumb-64168.uc.r.appspot.com/"

    fun getInstance(): Retrofit{
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}