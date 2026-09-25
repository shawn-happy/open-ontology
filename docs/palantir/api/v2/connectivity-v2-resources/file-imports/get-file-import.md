`GET /api/v2/connectivity/connections/{connectionRid}/fileImports/{fileImportRid}`

Get the FileImport with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-file-import-read`.

**OAuth2 scopes**: `api:connectivity-file-import-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `fileImportRid` | string | 是 | The Resource Identifier (RID) of a FileImport (also known as a batch sync).<br>示例: `ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158` |

## Response

**FileImport**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `FileImport` | object | 是 | 示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","importMode":"SNAPSHOT","displayName":"My file import","connectionRid":"ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b","branchName":"master","subfolder":"subfolder1/subfolder2","rid":"ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158","fileImportFilters":[{"type":"pathMatchesFilter","regex":"my-subfolder"}]}` |
| `FileImport.rid` | string | 是 | The Resource Identifier (RID) of a FileImport (also known as a batch sync).<br>示例: `ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158` |
| `FileImport.connectionRid` | string | 是 | The RID of the Connection (also known as a source) that the File Import uses to import data.<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `FileImport.datasetRid` | string | 是 | The RID of the output dataset. Can not be modified after the file import is created.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `FileImport.branchName` | string | 否 | The branch name in the output dataset that will contain the imported data. Defaults to `master` for most enrollments. Can not be modified after the file import is created.<br>示例: `master` |
| `FileImport.displayName` | string | 是 | 示例: `My file import` |
| `FileImport.fileImportFilters` | list<FileImportFilter> | 否 | Use filters to limit which files should be imported. Filters are applied in the order they are defined. A different ordering of filters may lead to a more optimized import. [Learn more about optimizing file imports.](/docs/foundry/data-connection/file-based-syncs/#optimize-file-based-syncs) |
| `FileImport.fileImportFilters.FileImportFilter` | union | 是 | [Filters](/docs/foundry/data-connection/file-based-syncs/#filters) allow you to filter source files<br>before they are imported into Foundry. |
| `FileImport.fileImportFilters.FileImportFilter.pathNotMatchesFilter` | object | 否 | Only import files whose path (relative to the root of the source) does not match the regular expression.<br>**Example**<br>Suppose we are importing files from `relative/subfolder`.<br>`relative/subfolder` contains:<br>- `relative/subfolder/include-file.txt`<br>- `relative/subfolder/exclude-file.txt`<br>- `relative/subfolder/other-file.txt`<br>With the `relative/subfolder/exclude-.*.txt` regex, both `relative/subfolder/include-file.txt` and `relative/subfolder/other-file.txt` will be imported,<br>and `relative/subfolder/exclude-file.txt` will be excluded from the import. |
| `FileImport.fileImportFilters.FileImportFilter.pathNotMatchesFilter.regex` | string | 是 | Must be written to match the paths relative to the root of the source, even if a subfolder is specified.<br>示例: `my-subfolder` |
| `FileImport.fileImportFilters.FileImportFilter.anyPathMatchesFilter` | object | 否 | If any file has a relative path matching the regular expression, sync all files in the subfolder that are not otherwise filtered. |
| `FileImport.fileImportFilters.FileImportFilter.anyPathMatchesFilter.regex` | string | 是 | The regular expression for the relative path to match against.<br>示例: `my-subfolder` |
| `FileImport.fileImportFilters.FileImportFilter.filesCountLimitFilter` | object | 否 | Only retain `filesCount` number of files in each transaction.<br>The choice of files to retain is made without any guarantee of order.<br>This option can increase the reliability of incremental syncs. |
| `FileImport.fileImportFilters.FileImportFilter.filesCountLimitFilter.filesCount` | integer | 是 | The number of files to import in the transaction. The value specified must be positive.<br>示例: `5` |
| `FileImport.fileImportFilters.FileImportFilter.changedSinceLastUploadFilter` | object | 否 | Only import files that have changed or been added since the last import run. Whether or not a file is considered to be changed is determined by the specified file properties.<br>This will exclude files uploaded in any previous imports, regardless of the file import mode used. A SNAPSHOT file import mode does not reset the filter. |
| `FileImport.fileImportFilters.FileImportFilter.changedSinceLastUploadFilter.fileProperties` | list<FileProperty> | 否 | The criteria on which to determine whether a file has been changed or not since the last import.<br>If any of the specified criteria have changed, the file is consider changed. The criteria include:<br>LAST_MODIFIED: The file's last modified timestamp has changed since the last import.<br>SIZE: The file's size has changed since the last import.<br>If no criteria are specified, only newly added files will be imported. |
| `FileImport.fileImportFilters.FileImportFilter.changedSinceLastUploadFilter.fileProperties.FileProperty` | enum | 是 | — |
| `FileImport.fileImportFilters.FileImportFilter.customFilter` | object | 否 | A custom file import filter. Custom file import filters can be fetched but cannot currently be used<br>when creating or updating file imports. |
| `FileImport.fileImportFilters.FileImportFilter.customFilter.config` | any | 是 | 示例: `{"type":"my-custom-file-filter","my-custom-property":"my value"}` |
| `FileImport.fileImportFilters.FileImportFilter.lastModifiedAfterFilter` | object | 否 | Only import files that have been modified after a specified timestamp |
| `FileImport.fileImportFilters.FileImportFilter.lastModifiedAfterFilter.afterTimestamp` | string | 否 | Timestamp threshold, specified in ISO-8601 format.<br>If not specified, defaults to the timestamp the filter is added to the file import.<br>示例: `2020-01-01T00:00:00Z` |
| `FileImport.fileImportFilters.FileImportFilter.pathMatchesFilter` | object | 否 | Only import files whose path (relative to the root of the source) matches the regular expression.<br>**Example**<br>Suppose we are importing files from `relative/subfolder`.<br>`relative/subfolder` contains:<br>- `relative/subfolder/include-file.txt`<br>- `relative/subfolder/exclude-file.txt`<br>- `relative/subfolder/other-file.txt`<br>With the `relative/subfolder/include-.*.txt` regex, only `relative/subfolder/include-file.txt` will be imported. |
| `FileImport.fileImportFilters.FileImportFilter.pathMatchesFilter.regex` | string | 是 | Must be written to match the paths relative to the root of the source, even if a subfolder is specified.<br>示例: `my-subfolder` |
| `FileImport.fileImportFilters.FileImportFilter.atLeastCountFilter` | object | 否 | Import all filtered files only if there are at least the specified number of files remaining. |
| `FileImport.fileImportFilters.FileImportFilter.atLeastCountFilter.minFilesCount` | integer | 是 | The minimum number of files remaining expected.<br>The value specified must be greater than 0.<br>示例: `5` |
| `FileImport.fileImportFilters.FileImportFilter.fileSizeFilter` | object | 否 | Only import files whose size is between the specified minimum and maximum values.<br>At least one of `gt` or `lt` should be present.<br>If both are present, the value specified for `gt` must be strictly less than `lt - 1`. |
| `FileImport.fileImportFilters.FileImportFilter.fileSizeFilter.gt` | string | 否 | File size must be greater than this number for it to be imported.<br>The value specified cannot be a negative number.<br>示例: `72526847` |
| `FileImport.fileImportFilters.FileImportFilter.fileSizeFilter.lt` | string | 否 | File size must be less than this number for it to be imported.<br>The value specified must be at least 1 byte.<br>示例: `72526847` |
| `FileImport.importMode` | enum | 是 | Import mode governs how raw files are read from an external system, and written into a Foundry dataset.<br>SNAPSHOT: Defines a new dataset state consisting only of files from a particular import execution.<br>APPEND: Purely additive and yields data from previous import executions in addition to newly added files.<br>UPDATE: Replaces existing files from previous import executions based on file names.<br>示例: `SNAPSHOT` |
| `FileImport.subfolder` | string | 否 | A subfolder in the external system that will be imported. If not specified, defaults to the root folder of the external system.<br>示例: `subfolder1/subfolder2` |

```json
{
  "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
  "importMode": "SNAPSHOT",
  "displayName": "My file import",
  "connectionRid": "ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b",
  "branchName": "master",
  "subfolder": "subfolder1/subfolder2",
  "rid": "ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158",
  "fileImportFilters": [
    {
      "type": "pathMatchesFilter",
      "regex": "my-subfolder"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `FileImportNotFound` | The given FileImport could not be found. |
