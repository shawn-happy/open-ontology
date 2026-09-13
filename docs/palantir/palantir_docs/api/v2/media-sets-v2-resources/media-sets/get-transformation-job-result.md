`GET /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}/transformationJobs/{transformationJobId}/result`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets the result of a completed transformation job. Returns the transformed media content as binary data.
This endpoint will return an error if the transformation job has not completed successfully.


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

**body**

The transformed media content.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | The transformed media content. |
