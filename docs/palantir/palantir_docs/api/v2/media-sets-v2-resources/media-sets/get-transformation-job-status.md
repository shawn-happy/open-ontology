`GET /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}/transformationJobs/{transformationJobId}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets the status of a transformation job.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-transform`.

**OAuth2 scopes**: `api:mediasets-transform`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The RID of the media set. |
| `mediaItemRid` | string | 是 | The RID of the media item. |
| `transformationJobId` | string | 是 | The ID of the transformation job. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Response

**GetTransformationJobStatusResponse**

The status of the transformation job.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetTransformationJobStatusResponse` | object | 是 | The status of the transformation job. |
| `GetTransformationJobStatusResponse.status` | enum | 是 | The status of a transformation job. |
| `GetTransformationJobStatusResponse.jobId` | string | 是 | An identifier for a media item transformation job. |
