`POST /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}/transform`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Initiates a transformation on a media item. Returns a job ID that can be used to check the status and retrieve 
the result of the transformation.

Transforming a media item requires that you are able to read the media item, either via `api:mediasets-read` or
via a `MediaItemReadToken`


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-transform`.

**OAuth2 scopes**: `api:mediasets-transform`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The RID of the media set. |
| `mediaItemRid` | string | 是 | The RID of the media item. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Request body

```json
{
  "transformation": {
    "type": "image",
    "encoding": {
      "type": "webp"
    },
    "operations": [
      {
        "type": "resize",
        "width": 800,
        "height": 600
      }
    ]
  }
}
```

## Response

**TransformMediaItemResponse**

The transformation was initiated successfully.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `TransformMediaItemResponse` | object | 是 | The transformation was initiated successfully. |
| `TransformMediaItemResponse.status` | enum | 是 | The status of a transformation job. |
| `TransformMediaItemResponse.jobId` | string | 是 | An identifier for a media item transformation job. |
