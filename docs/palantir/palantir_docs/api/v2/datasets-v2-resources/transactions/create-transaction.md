`POST /api/v2/datasets/{datasetRid}/transactions`

Creates a Transaction on a Branch of a Dataset.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-write`.

**OAuth2 scopes**: `api:datasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch on which to create the Transaction. Defaults to `master` for most enrollments.<br>示例: `master` |

## Request body

```json
{
  "transactionType": "APPEND"
}
```

## Response

**Transaction**

The created Transaction

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Transaction` | object | 是 | The created Transaction<br>示例: `{"transactionType":"APPEND","createdTime":"2020-09-30T14:30:00Z","rid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","closedTime":"2020-09-30T21:00:00Z","status":"COMMITTED"}` |
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
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| CONFLICT | `OpenTransactionAlreadyExists` | A transaction is already open on this dataset and branch. A branch of a dataset can only have one open transaction at a time. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
| PERMISSION_DENIED | `CreateTransactionPermissionDenied` | Could not create the Transaction. |
