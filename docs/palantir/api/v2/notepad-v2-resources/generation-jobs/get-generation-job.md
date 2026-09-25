`GET /api/v2/notepad/templates/{templateRid}/generationJobs/{generationJobRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Load an existing GenerationJob. This is used to monitor job progress.


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

## Response

**GenerationJob**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GenerationJob` | object | 是 | 示例: `{"rid":"ri.notepad.main.generation-job.ab12c039-353c-4555-9704-eacfdfaa2c1c","status":{"type":"running"}}` |
| `GenerationJob.rid` | string | 是 | The unique identifier for a GenerationJob<br>示例: `ri.notepad.main.generation-job.ab12c039-353c-4555-9704-eacfdfaa2c1c` |
| `GenerationJob.status` | union | 是 | The status of a GenerationJob<br>示例: `{"type":"running"}` |
| `GenerationJob.status.running` | object | 否 | The generation job is currently running |
| `GenerationJob.status.failed` | object | 否 | The generation job failed |
| `GenerationJob.status.failed.errorMessage` | string | 是 | The error message explaining why template generation failed |
| `GenerationJob.status.succeeded` | object | 否 | The generation job succeeded |

```json
{
  "rid": "ri.notepad.main.generation-job.ab12c039-353c-4555-9704-eacfdfaa2c1c",
  "status": {
    "type": "running"
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `GenerationJobNotFound` | The given GenerationJob could not be found. |
