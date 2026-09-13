`GET /api/v2/notepad/exportJobs/{exportJobRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Loads an ExportJob. This endpoint is used to monitor job progress.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:notepad-export`.

**OAuth2 scopes**: `api:notepad-export`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `exportJobRid` | string | 是 | The unique identifier for an ExportJob<br>示例: `ri.notepad.main.export-job.ef32c039-353c-4555-9704-eacfdfaa2c1c` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ExportJob**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ExportJob` | object | 是 | 示例: `{"rid":"ri.notepad.main.export-job.ef32c039-353c-4555-9704-eacfdfaa2c1c","status":{"type":"running"}}` |
| `ExportJob.rid` | string | 是 | The unique identifier for an ExportJob<br>示例: `ri.notepad.main.export-job.ef32c039-353c-4555-9704-eacfdfaa2c1c` |
| `ExportJob.status` | union | 是 | The status of an export job<br>示例: `{"type":"running"}` |
| `ExportJob.status.running` | object | 否 | The export job is currently running |
| `ExportJob.status.failed` | object | 否 | The export job failed |
| `ExportJob.status.failed.errorMessage` | string | 是 | The error message explaining why the export failed |
| `ExportJob.status.failed.errorCode` | string | 否 | — |
| `ExportJob.status.failed.errorInstanceId` | string | 否 | — |
| `ExportJob.status.succeeded` | object | 否 | The export job succeeded |
| `ExportJob.status.succeeded.fileRid` | string | 是 | The File containing the exported content<br>示例: `ri.notepad.main.file.cf32c039-353c-4555-9704-eacfdfaa2c1c` |

```json
{
  "rid": "ri.notepad.main.export-job.ef32c039-353c-4555-9704-eacfdfaa2c1c",
  "status": {
    "type": "running"
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ExportJobNotFound` | The given ExportJob could not be found. |
