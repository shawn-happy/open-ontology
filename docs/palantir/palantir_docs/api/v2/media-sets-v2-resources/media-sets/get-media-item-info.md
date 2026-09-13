`GET /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}`

Gets information about the media item.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-read`.

**OAuth2 scopes**: `api:mediasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The RID of the media set. |
| `mediaItemRid` | string | 是 | The RID of the media item. |

## Response

**GetMediaItemInfoResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetMediaItemInfoResponse` | object | 是 | 示例: `{"viewRid":"ri.mio.main.view.1","logicalTimestamp":12345,"path":"example.png","attribution":{"creatorId":1,"creationTimestamp":"2020-07-10 15:00:00.000"},"originallyUploadedFileMimeType":"image/png"}` |
| `GetMediaItemInfoResponse.viewRid` | string | 是 | The Resource Identifier (RID) of a single View of a Media Set. A Media Set View is an independent collection of Media Items. |
| `GetMediaItemInfoResponse.path` | string | 否 | A user-specified identifier for a media item within a media set.<br>Paths must be less than 256 characters long.<br>If multiple items are written to the same media set at the same path, then when retrieving by path the media<br>item which was written last is returned. |
| `GetMediaItemInfoResponse.logicalTimestamp` | string | 是 | A number representing a logical ordering to be used for transactions, etc.<br>This can be interpreted as a timestamp in microseconds, but may differ slightly from system clock time due<br>to clock drift and slight adjustments for the sake of ordering.<br>Only positive timestamps (representing times after epoch) are supported. |
| `GetMediaItemInfoResponse.attribution` | object | 否 | — |
| `GetMediaItemInfoResponse.attribution.creatorId` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `GetMediaItemInfoResponse.attribution.creationTimestamp` | string | 是 | The timestamp when the media item was created, in ISO 8601 timestamp format. |
| `GetMediaItemInfoResponse.originallyUploadedFileMimeType` | string | 否 | The [media type](https://www.iana.org/assignments/media-types/media-types.xhtml) of the file or attachment.<br>Examples: `application/json`, `application/pdf`, `application/octet-stream`, `image/jpeg`<br>示例: `application/pdf` |
| `GetMediaItemInfoResponse.mimeType` | string | 否 | The [media type](https://www.iana.org/assignments/media-types/media-types.xhtml) of the file or attachment.<br>Examples: `application/json`, `application/pdf`, `application/octet-stream`, `image/jpeg`<br>示例: `application/pdf` |
| `GetMediaItemInfoResponse.sizeBytes` | integer | 否 | The size of the media item in bytes. |

```json
{
  "viewRid": "ri.mio.main.view.1",
  "logicalTimestamp": 12345,
  "path": "example.png",
  "attribution": {
    "creatorId": 1,
    "creationTimestamp": "2020-07-10 15:00:00.000"
  },
  "originallyUploadedFileMimeType": "image/png"
}
```
