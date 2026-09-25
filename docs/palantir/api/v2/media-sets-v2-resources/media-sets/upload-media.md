`PUT /api/v2/mediasets/media/upload`

Uploads a temporary media item. If the media item isn't persisted within 1 hour, the item will be deleted. 

If multiple resources are attributed to, usage will be attributed to the first one in the list.

The body of the request must contain the binary content of the file and the `Content-Type` header must be `application/octet-stream`.
Third-party applications using this endpoint via OAuth2 must request the following operation scopes: `api:ontologies-read api:ontologies-write`.

**OAuth2 scopes**: `api:ontologies-read` `api:ontologies-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `filename` | string | 是 | A user-defined label for a media item within a media set. Required if the backing media set requires paths.<br>Uploading multiple files to the same path will result in only the most recent file being associated with the<br>path.<br>示例: `my-file.png` |
| `mediaItemRid` | string | 否 | An optional RID to use for the media item to create. If omitted, the server will automatically generate a<br>RID. In most cases, the server-generated RID should be preferred; only specify a custom RID if your<br>workflow strictly requires deterministic or client-controlled identifiers.<br>The RID must be in the format of `ri.mio.<instance>.media-item.<UUID>`, where `<instance>` is the same as<br>the instance part of the media set RID, and `<UUID>` is a UUID.<br>An `InvalidMediaItemRid` error will be thrown if the RID is not in the expected format.<br>A `MediaItemRidAlreadyExists` error will be thrown if the media set already contains a media item with the same RID. |

## Request body

## Response

**MediaReference**

The media reference for the uploaded media.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `MediaReference` | object | 是 | The media reference for the uploaded media. |
| `MediaReference.mimeType` | string | 是 | The [media type](https://www.iana.org/assignments/media-types/media-types.xhtml) of the file or attachment.<br>Examples: `application/json`, `application/pdf`, `application/octet-stream`, `image/jpeg`<br>示例: `application/pdf` |
| `MediaReference.reference` | union | 是 | A union of the types supported by media reference properties. |
| `MediaReference.reference.mediaSetViewItem` | object | 否 | — |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem` | object | 是 | — |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.mediaSetViewRid` | string | 是 | The Resource Identifier (RID) of a single View of a Media Set. A Media Set View is an independent collection of Media Items. |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.mediaItemRid` | string | 是 | The Resource Identifier (RID) of an individual Media Item within a Media Set in Foundry. |
| `MediaReference.reference.mediaSetViewItem.mediaSetViewItem.token` | string | 否 | A token that grants access to read specific media items. |
