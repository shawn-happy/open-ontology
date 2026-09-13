`GET /api/v2/datasets/{datasetRid}/branches/{branchName}/transactions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the Transaction history for the given Dataset. When requesting all transactions, the endpoint returns them in reverse chronological order.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `branchName` | string | 是 | The name of a Branch.<br>示例: `master` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The default pageSize is 20 transactions and the maximum allowed pageSize is 50 transactions |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListTransactionsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListTransactionsResponse` | object | 是 | 示例: `{"data":[{"transactionType":"APPEND","createdTime":"2020-09-30T14:30:00Z","rid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","closedTime":"2020-09-30T21:00:00Z","status":"COMMITTED"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListTransactionsResponse.data` | list<Transaction> | 否 | — |
| `ListTransactionsResponse.data.Transaction` | object | 是 | — |
| `ListTransactionsResponse.data.Transaction.rid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `ListTransactionsResponse.data.Transaction.transactionType` | enum | 是 | The type of a Transaction.<br>示例: `APPEND` |
| `ListTransactionsResponse.data.Transaction.status` | enum | 是 | The status of a Transaction.<br>示例: `COMMITTED` |
| `ListTransactionsResponse.data.Transaction.createdTime` | string | 是 | The timestamp when the transaction was created, in ISO 8601 timestamp format.<br>示例: `2020-09-30T14:30:00Z` |
| `ListTransactionsResponse.data.Transaction.closedTime` | string | 否 | The timestamp when the transaction was closed, in ISO 8601 timestamp format.<br>示例: `2020-09-30T21:00:00Z` |
| `ListTransactionsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "transactionType": "APPEND",
      "createdTime": "2020-09-30T14:30:00Z",
      "rid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
      "closedTime": "2020-09-30T21:00:00Z",
      "status": "COMMITTED"
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
| PERMISSION_DENIED | `GetBranchTransactionHistoryPermissionDenied` | Could not transactions the Branch. |
