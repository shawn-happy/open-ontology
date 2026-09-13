`GET /api/v2/datasets/{datasetRid}/files`

Lists Files contained in a Dataset. By default files are listed on the latest view of the default 
branch - `master` for most enrollments.
#### Advanced Usage
See [Datasets Core Concepts](/docs/foundry/data-integration/datasets/) for details on using branches and transactions.
To **list files on a specific Branch** specify the Branch's name as `branchName`. This will include the most
recent version of all files since the latest snapshot transaction, or the earliest ancestor transaction of the 
branch if there are no snapshot transactions.
To **list files on the resolved view of a transaction** specify the Transaction's resource identifier
as `endTransactionRid`. This will include the most recent version of all files since the latest snapshot
transaction, or the earliest ancestor transaction if there are no snapshot transactions.
To **list files on the resolved view of a range of transactions** specify the the start transaction's resource
identifier as `startTransactionRid` and the end transaction's resource identifier as `endTransactionRid`. This
will include the most recent version of all files since the `startTransactionRid` up to the `endTransactionRid`.
Note that an intermediate snapshot transaction will remove all files from the view. Behavior is undefined when 
the start and end transactions do not belong to the same root-to-leaf path.
To **list files on a specific transaction** specify the Transaction's resource identifier as both the 
`startTransactionRid` and `endTransactionRid`. This will include only files that were modified as part of that
Transaction.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch on which to list Files. Defaults to `master` for most enrollments.<br>示例: `master` |
| `pathPrefix` | string | 否 | When present returns only files in the dataset whose path starts with this value. If pathPrefix matches a file exactly, returns just that file.<br>示例: `My Folder/my-file.csv` |
| `startTransactionRid` | string | 否 | The Resource Identifier (RID) of the start Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `endTransactionRid` | string | 否 | The Resource Identifier (RID) of the end Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListFilesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListFilesResponse` | object | 是 | 示例: `{"data":[{"path":"2020/09/30/trades.csv","updatedTime":"2020-09-30T12:34:56.789Z","transactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListFilesResponse.data` | list<File> | 否 | — |
| `ListFilesResponse.data.File` | object | 是 | — |
| `ListFilesResponse.data.File.path` | string | 是 | The path to a File within Foundry. Paths are relative and must not start with a leading slash.<br>Examples: `my-file.txt`, `path/to/my-file.jpg`, `dataframe.snappy.parquet`.<br>示例: `My Folder/my-file.csv` |
| `ListFilesResponse.data.File.transactionRid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `ListFilesResponse.data.File.sizeBytes` | string | 否 | — |
| `ListFilesResponse.data.File.updatedTime` | string | 是 | 示例: `2020-09-30T12:34:56.789Z` |
| `ListFilesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "path": "2020/09/30/trades.csv",
      "updatedTime": "2020-09-30T12:34:56.789Z",
      "transactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4"
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
| INVALID_ARGUMENT | `InvalidParameterCombination` | The given parameters are individually valid but cannot be used in the given combination. |
| INVALID_ARGUMENT | `InvalidPageSize` | The provided page size was zero or negative. Page sizes must be greater than zero. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
| INVALID_ARGUMENT | `InvalidFilePath` | The provided file path is invalid. Check that the path does not start with a leading slash. |
