`POST /api/v2/notepad/templates/{templateRid}/generationJobs/{generationJobRid}/saveDocument`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Save generated content as a new notepad document. This is only possible if the GenerationJob succeeded.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:notepad-write`.

**OAuth2 scopes**: `api:notepad-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `templateRid` | string | 是 | The unique identifier for a Template<br>示例: `ri.notepad.main.notepad-template.bef90a51-d37d-4983-abde-56e5bd0fcf52` |
| `generationJobRid` | string | 是 | The unique identifier for a GenerationJob<br>示例: `ri.notepad.main.generation-job.ab12c039-353c-4555-9704-eacfdfaa2c1c` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"
}
```

## Response

**SaveDocumentResponse**

Response for saving a document

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `SaveDocumentResponse` | object | 是 | Response for saving a document<br>示例: `{"documentRid":"ri.notepad.main.notepad.ef32c039-353c-4555-9704-eacfdfaa2c1c"}` |
| `SaveDocumentResponse.documentRid` | string | 是 | The RID of the newly created document<br>示例: `ri.notepad.main.notepad.ef32c039-353c-4555-9704-eacfdfaa2c1c` |

```json
{
  "documentRid": "ri.notepad.main.notepad.ef32c039-353c-4555-9704-eacfdfaa2c1c"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| FAILED_PRECONDITION | `GenerationJobStatusFailed` | The operation cannot be completed because the generation job has failed status. |
| FAILED_PRECONDITION | `GenerationJobStatusRunning` | The operation cannot be completed because the generation job has running status. |
| INVALID_ARGUMENT | `InvalidDisplayName` | The display name of a resource should not be exactly `.` or `..`, contain a forward slash `/` and must be<br>less than or equal to 700 characters. |
| CONFLICT | `ResourceNameAlreadyExists` | The provided resource name is already in use by another resource in the same folder. |
| INVALID_ARGUMENT | `InvalidFolder` | The given resource is not a Folder. |
| PERMISSION_DENIED | `SaveDocumentGenerationJobPermissionDenied` | Could not saveDocument the GenerationJob. |
| NOT_FOUND | `FolderNotFound` | The given Folder could not be found. |
