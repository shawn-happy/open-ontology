`GET /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}/reference`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets the [media reference](/docs/foundry/data-integration/media-sets/#media-references) for this media item.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-read`.

**OAuth2 scopes**: `api:mediasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The RID of the media set. |
| `mediaItemRid` | string | 是 | The RID of the media item. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Response

**MediaReference**

The representation of a media reference.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `MediaReference` | object | 是 | The representation of a media reference. |
| `MediaReference.mimeType` | string | 是 | The [media type](https://www.iana.org/assignments/media-types/media-types.xhtml) of the file or attachment.<br>Examples: `application/json`, `application/pdf`, `application/octet-stream`, `image/jpeg`<br>示例: `application/pdf` |
| `MediaReference.reference` | union | 是 | A union of the types supported by media reference properties. |
| `MediaReference.reference.mediaSetViewItem` | object | 否 | — |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem` | object | 是 | — |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.mediaSetViewRid` | string | 是 | The Resource Identifier (RID) of a single View of a Media Set. A Media Set View is an independent collection of Media Items. |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.mediaItemRid` | string | 是 | The Resource Identifier (RID) of an individual Media Item within a Media Set in Foundry. |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.token` | string | 否 | A token that grants access to read specific media items. |
