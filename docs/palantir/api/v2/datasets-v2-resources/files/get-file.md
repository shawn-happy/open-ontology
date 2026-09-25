`GET /api/v2/datasets/{datasetRid}/files/{filePath}`

Gets metadata about a File contained in a Dataset. By default this retrieves the file's metadata from the latest
view of the default branch - `master` for most enrollments.
#### Advanced Usage
See [Datasets Core Concepts](/docs/foundry/data-integration/datasets/) for details on using branches and transactions. 
To **get a file's metadata from a specific Branch** specify the Branch's name as `branchName`. This will 
retrieve metadata for the most recent version of the file since the latest snapshot transaction, or the earliest
ancestor transaction of the branch if there are no snapshot transactions.
To **get a file's metadata from the resolved view of a transaction** specify the Transaction's resource identifier
as `endTransactionRid`. This will retrieve metadata for the most recent version of the file since the latest snapshot
transaction, or the earliest ancestor transaction if there are no snapshot transactions.
To **get a file's metadata from the resolved view of a range of transactions** specify the the start transaction's
resource identifier as `startTransactionRid` and the end transaction's resource identifier as `endTransactionRid`.
This will retrieve metadata for the most recent version of the file since the `startTransactionRid` up to the 
`endTransactionRid`. Behavior is undefined when the start and end transactions do not belong to the same root-to-leaf path.
To **get a file's metadata from a specific transaction** specify the Transaction's resource identifier as both the 
`startTransactionRid` and `endTransactionRid`.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `filePath` | string | 是 | The path to a File within Foundry. Paths are relative and must not start with a leading slash.<br>Examples: `my-file.txt`, `path/to/my-file.jpg`, `dataframe.snappy.parquet`.<br>示例: `My Folder/my-file.csv` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch that contains the File. Defaults to `master` for most enrollments.<br>示例: `master` |
| `startTransactionRid` | string | 否 | The Resource Identifier (RID) of the start Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `endTransactionRid` | string | 否 | The Resource Identifier (RID) of the end Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |

## Response

**File**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `File` | object | 是 | 示例: `{"path":"2020/09/30/trades.csv","updatedTime":"2020-09-30T12:34:56.789Z","transactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4"}` |
| `File.path` | string | 是 | The path to a File within Foundry. Paths are relative and must not start with a leading slash.<br>Examples: `my-file.txt`, `path/to/my-file.jpg`, `dataframe.snappy.parquet`.<br>示例: `My Folder/my-file.csv` |
| `File.transactionRid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `File.sizeBytes` | string | 否 | — |
| `File.updatedTime` | string | 是 | 示例: `2020-09-30T12:34:56.789Z` |

```json
{
  "path": "2020/09/30/trades.csv",
  "updatedTime": "2020-09-30T12:34:56.789Z",
  "transactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `FileNotFoundOnBranch` | The requested file could not be found on the given branch, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| NOT_FOUND | `FileNotFoundOnTransactionRange` | The requested file could not be found on the given transaction range, or the client token does not have access to it. |
| INVALID_ARGUMENT | `InvalidParameterCombination` | The given parameters are individually valid but cannot be used in the given combination. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| NOT_FOUND | `FileNotFound` | The given File could not be found. |
