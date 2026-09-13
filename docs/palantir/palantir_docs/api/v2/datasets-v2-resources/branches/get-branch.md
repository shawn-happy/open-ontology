`GET /api/v2/datasets/{datasetRid}/branches/{branchName}`

Get a Branch of a Dataset.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `branchName` | string | 是 | The name of a Branch.<br>示例: `master` |

## Response

**Branch**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Branch` | object | 是 | 示例: `{"transactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","name":"master"}` |
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
