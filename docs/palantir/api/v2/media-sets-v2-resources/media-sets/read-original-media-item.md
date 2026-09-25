`GET /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}/original`

Gets the content of an original file uploaded to the media item, even if it was transformed on upload due to being an additional input format.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-read`.

**OAuth2 scopes**: `api:mediasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `mediaItemRid` | string | 是 | The Resource Identifier (RID) of an individual Media Item within a Media Set in Foundry. |

## Response

**body**

The content stream.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | The content stream. |
