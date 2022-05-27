package Model

data class TransactionParentItemModel(var date: String, val recipientList :List<TransactionChildItemModel>)

data class TransactionChildItemModel(val name: String, val accountNo: String, val amount: Double)
