`POST /api/v2/datasets/{datasetRid}/branches`

Creates a branch on an existing dataset. A branch may optionally point to a (committed) transaction.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-write`.

**OAuth2 scopes**: `api:datasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Request body

```json
{
  "transactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
  "name": "master"
}
```

## Response

**Branch**

The created Branch

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Branch` | object | 是 | The created Branch<br>示例: `{"transactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","name":"master"}` |
| `Branch.name` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Branch.transactionRid` | string | 否 | The most recent OPEN or COMMITTED transaction on the branch. This will never be an ABORTED transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |

```json
{
  "transactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
  "name": "master"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| INVALID_ARGUMENT | `TransactionNotCommitted` | The given transaction has not been committed. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| PERMISSION_DENIED | `CreateBranchPermissionDenied` | The provided token does not have permission to create a branch of this dataset. |
| CONFLICT | `BranchAlreadyExists` | The branch cannot be created because a branch with that name already exists. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
