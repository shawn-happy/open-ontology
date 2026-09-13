`POST /api/v2/orchestration/builds/{buildRid}/cancel`

Request a cancellation for all unfinished jobs in a build. The build's status will not update immediately. This endpoint is asynchronous and a success response indicates that the cancellation request has been acknowledged and the build is expected to be canceled soon. If the build has already finished or finishes shortly after the request and before the cancellation, the build will not change.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-write`.

**OAuth2 scopes**: `api:orchestration-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `buildRid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `CancelBuildPermissionDenied` | Could not cancel the Build. |
