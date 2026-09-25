`POST /api/v2/orchestration/schedules/{scheduleRid}/unpause`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-write`.

**OAuth2 scopes**: `api:orchestration-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `UnpauseSchedulePermissionDenied` | Could not unpause the Schedule. |
