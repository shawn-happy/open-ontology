`POST /api/v2/datasets/views/{viewDatasetRid}/removeBackingDatasets`

Removes specified backing datasets from a View. Removing a dataset triggers a 
[SNAPSHOT](/docs/foundry/data-integration/datasets#snapshot) transaction on the next update. If a 
specified dataset does not exist, no error is thrown.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-write`.

**OAuth2 scopes**: `api:datasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `viewDatasetRid` | string | 是 | The rid of the View.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Request body

```json
{
  "backingDatasets": [
    {
      "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
      "stopPropagatingMarkingIds": [
        "18212f9a-0e63-4b79-96a0-aae04df23336"
      ],
      "branch": "master"
    }
  ],
  "branch": "master"
}
```

## Response

**View**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `View` | object | 是 | 示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","parentFolderRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791","viewName":"My Dataset","backingDatasets":[{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","stopPropagatingMarkingIds":["18212f9a-0e63-4b79-96a0-aae04df23336"],"branch":"master"}],"branch":"master","primaryKey":{"columns":["order_id"]}}` |
| `View.viewName` | string | 是 | 示例: `My Dataset` |
| `View.datasetRid` | string | 是 | The rid of the View.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `View.parentFolderRid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791` |
| `View.branch` | string | 否 | The branch name of the View. If not specified, defaults to `master` for most enrollments.<br>示例: `master` |
| `View.backingDatasets` | list<ViewBackingDataset> | 否 | — |
| `View.backingDatasets.ViewBackingDataset` | object | 是 | One of the Datasets backing a View. |
| `View.backingDatasets.ViewBackingDataset.branch` | string | 否 | The branch of the backing dataset. If not specified, defaults to the branch of the View.<br>示例: `master` |
| `View.backingDatasets.ViewBackingDataset.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `View.backingDatasets.ViewBackingDataset.stopPropagatingMarkingIds` | list<MarkingId> | 否 | Markings listed here will not be inherited from this backing dataset. The caller must have the DECLASSIFY<br>permission on each marking listed here. If multiple backing datasets have the same marking applied,<br>the marking must be listed for each backing dataset or it will still be inherited. |
| `View.backingDatasets.ViewBackingDataset.stopPropagatingMarkingIds.MarkingId` | string | 是 | The ID of a security marking. |
| `View.primaryKey` | object | 否 | The primary key of the dataset. Primary keys are treated as guarantees provided by the creator of the<br>dataset.<br>示例: `{"columns":["order_id"]}` |
| `View.primaryKey.columns` | list<string> | 否 | The columns that constitute the primary key. These columns must satisfy the following constraints:<br>- The list of columns must be non-empty.<br>- The list must not contain duplicate columns after applying column normalization.<br>- Each referenced column must exist in the schema.<br>- The type of each referenced column must be one of the following: `BYTE`, `SHORT`, `DECIMAL`, `INTEGER`,<br>`LONG`, `STRING`, `BOOLEAN`, `TIMESTAMP` or `DATE`.<br>示例: `["order_id"]` |
| `View.primaryKey.resolution` | union | 是 | The semantics of the primary key within the dataset. For example, the unique resolution means that every<br>row in the dataset has a distinct primary key. The value of this field represents a contract for writers<br>of the dataset. Writers are responsible for maintaining any related invariants, and readers may make<br>optimizations based on this. Violating the assumptions of the resolution can cause undefined behavior,<br>for example, having duplicate primary keys with the unique resolution. |
| `View.primaryKey.resolution.unique` | object | 否 | Primary key values are unique within the dataset â no conflicts. |
| `View.primaryKey.resolution.duplicate` | object | 否 | Duplicate primary key values may exist within the dataset â resolution required. |
| `View.primaryKey.resolution.duplicate.deletionColumn` | string | 否 | The name of the boolean column that indicates whether a row should be considered deleted. Based on the<br>`resolutionStrategy`, if the final row selected for a given primary key has `true` in this column, that<br>row will be excluded from the results. Otherwise, it will be included.<br>示例: `my_column` |
| `View.primaryKey.resolution.duplicate.resolutionStrategy` | union | 是 | — |
| `View.primaryKey.resolution.duplicate.resolutionStrategy.latestWins` | object | 否 | Picks the row with the highest value of a list of columns, compared in order. |
| `View.primaryKey.resolution.duplicate.resolutionStrategy.latestWins.columns` | list<string> | 否 | 示例: `["colB","colC"]` |

```json
{
  "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "viewName": "My Dataset",
  "backingDatasets": [
    {
      "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
      "stopPropagatingMarkingIds": [
        "18212f9a-0e63-4b79-96a0-aae04df23336"
      ],
      "branch": "master"
    }
  ],
  "branch": "master",
  "primaryKey": {
    "columns": [
      "order_id"
    ]
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidViewBackingDataset` | Either you do not have access to one or more of the backing datasets or it does not exist. |
| NOT_FOUND | `ViewNotFound` | The requested View could not be found. Either the view does not exist, the branch is not valid or the<br>client token does not have access to it. |
| INVALID_ARGUMENT | `InputBackingDatasetNotInOutputViewProject` | One or more backing datasets do not live in the same project as the view. Add the missing datasets as<br>project resource references to the view's project using the Filesystem API, or move them into the view's<br>project, and then retry. |
| PERMISSION_DENIED | `RemoveBackingDatasetsPermissionDenied` | Could not removeBackingDatasets the View. |
