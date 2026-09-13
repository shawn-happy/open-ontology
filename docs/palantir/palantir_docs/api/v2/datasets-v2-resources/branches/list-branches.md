`GET /api/v2/datasets/{datasetRid}/branches`

Lists the Branches of a Dataset.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListBranchesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListBranchesResponse` | object | 是 | 示例: `{"data":[{"transactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","name":"master"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListBranchesResponse.data` | list<Branch> | 否 | — |
| `ListBranchesResponse.data.Branch` | object | 是 | — |
| `ListBranchesResponse.data.Branch.name` | string | 是 | The name of a Branch.<br>示例: `master` |
| `ListBranchesResponse.data.Branch.transactionRid` | string | 否 | The most recent OPEN or COMMITTED transaction on the branch. This will never be an ABORTED transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `ListBranchesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "transactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
      "name": "master"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
