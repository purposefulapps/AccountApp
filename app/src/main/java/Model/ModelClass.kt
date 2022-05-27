import java.text.DateFormat

data class LoginResponses(
    val status: String,
    val token: String,
    val username: String,
    val accountNo: String
)

data class Balance(
    val status: String,
    val accountNo: String,
    val balance: Double
)

data class Payees(
    val status: String,
    val data: List<Payee>
) {
    data class Payee(
        val id: String,
        val name: String,
        val accountNo: String
    )
}

data class Transactions(
    val status: String,
    val data: List<TransactionDetails>
    ){
    data class TransactionDetails(
        val transactionId: String,
        val amount: Double,
        val transactionDate: String,
        val description: String,
        val transactionType: String,
        val receipient: Receipient
    ){
        data class Receipient(
            val accountNo: String,
            val accountHolder: String
        )
    }
}
