`POST /api/v2/notepad/templates/{templateRid}/generate`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new GenerationJob. The template generation job will produce new document content by applying 
template parameters to an existing template. If the GenerationJob succeeds, the resulting contents can
be saved as a new Document or exported to a File.

The user must have the api:notepad-write scope to create GenerationJobs. Once created a GenerationJob
is only accessible to the user that created it.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:notepad-write`.

**OAuth2 scopes**: `api:notepad-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `templateRid` | string | 是 | The unique identifier for a Template<br>示例: `ri.notepad.main.notepad-template.bef90a51-d37d-4983-abde-56e5bd0fcf52` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "templateParameters": {
    "customerName": {
      "type": "string",
      "value": "John Doe"
    }
  },
  "templateVersion": 42
}
```

## Response

**GenerationJobRid**

The unique identifier for a GenerationJob

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GenerationJobRid` | string | 是 | The unique identifier for a GenerationJob<br>示例: `ri.notepad.main.generation-job.ab12c039-353c-4555-9704-eacfdfaa2c1c` |

```text
ri.notepad.main.generation-job.ab12c039-353c-4555-9704-eacfdfaa2c1c
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `TemplateNotFound` | The requested template was not found. |
| INVALID_ARGUMENT | `InvalidTimezone` | The provided timezone is not valid. |
| INVALID_ARGUMENT | `InvalidGenerationJobTemplateVersion` | The provided template version doesn't exist or the template has no published versions. |
| INVALID_ARGUMENT | `MissingGenerationJobTemplateParameters` | One or more template parameters are missing. |
| INVALID_ARGUMENT | `InvalidGenerationJobTemplateParameter` | A template parameter value is invalid (for example, is of the wrong type). |
| PERMISSION_DENIED | `GenerateTemplatePermissionDenied` | Could not generate the Template. |
