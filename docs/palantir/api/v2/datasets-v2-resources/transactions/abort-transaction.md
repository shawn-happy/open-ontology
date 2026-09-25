`POST /api/v2/datasets/{datasetRid}/transactions/{transactionRid}/abort`

Aborts an open Transaction. File modifications made on this Transaction are not preserved and the Branch is
not updated.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-write`.

**OAuth2 scopes**: `api:datasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `transactionRid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |

## Response

**Transaction**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Transaction` | object | 是 | 示例: `{"transactionType":"APPEND","createdTime":"2020-09-30T14:30:00Z","rid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","closedTime":"2020-09-30T21:00:00Z","status":"COMMITTED"}` |
| `Transaction.rid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `Transaction.transactionType` | enum | 是 | The type of a Transaction.<br>示例: `APPEND` |
| `Transaction.status` | enum | 是 | The status of a Transaction.<br>示例: `COMMITTED` |
| `Transaction.createdTime` | string | 是 | The timestamp when the transaction was created, in ISO 8601 timestamp format.<br>示例: `2020-09-30T14:30:00Z` |
| `Transaction.closedTime` | string | 否 | The timestamp when the transaction was closed, in ISO 8601 timestamp format.<br>示例: `2020-09-30T21:00:00Z` |

```json
{
  "transactionType": "APPEND",
  "createdTime": "2020-09-30T14:30:00Z",
  "rid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
  "closedTime": "2020-09-30T21:00:00Z",
  "status": "COMMITTED"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| INVALID_ARGUMENT | `TransactionNotOpen` | The given transaction is not open. |
| PERMISSION_DENIED | `AbortTransactionPermissionDenied` | Could not abort the Transaction. |
