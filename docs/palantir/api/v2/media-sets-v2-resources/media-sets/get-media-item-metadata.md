`GET /api/v2/mediasets/{mediaSetRid}/items/{mediaItemRid}/metadata`

Gets detailed metadata about the media item, including type-specific information
such as dimensions for images, duration for audio/video, page count for documents, etc.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-read`.

**OAuth2 scopes**: `api:mediasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The RID of the media set. |
| `mediaItemRid` | string | 是 | The RID of the media item. |

## Response

**MediaItemMetadata**

Detailed metadata about a media item, including type-specific information such as dimensions for images,<br>duration for audio/video, page count for documents, etc.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `MediaItemMetadata` | union | 是 | Detailed metadata about a media item, including type-specific information such as dimensions for images,<br>duration for audio/video, page count for documents, etc.<br>示例: `{"type":"imagery","format":"PNG","dimensions":{"width":1920,"height":1080},"sizeBytes":2048576}` |
| `MediaItemMetadata.cad` | object | 否 | Metadata for CAD media items. |
| `MediaItemMetadata.cad.format` | enum | 是 | The format of a CAD media item. |
| `MediaItemMetadata.cad.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.cad.units` | object | 否 | Units declared in a CAD file. |
| `MediaItemMetadata.cad.units.lengthUnit` | string | 否 | Raw declared length unit name, for example MILLIMETRE, METRE, INCH, or FOOT. Consumers should match<br>case-insensitively and tolerate unknown values. |
| `MediaItemMetadata.document` | object | 否 | Metadata for document media items. |
| `MediaItemMetadata.document.format` | enum | 是 | The format of a document media item. |
| `MediaItemMetadata.document.pages` | integer | 否 | The number of pages in the document. |
| `MediaItemMetadata.document.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.document.title` | string | 否 | The title of the document, if available. |
| `MediaItemMetadata.document.author` | string | 否 | The author of the document, if available. |
| `MediaItemMetadata.imagery` | object | 否 | Metadata for imagery (image) media items. |
| `MediaItemMetadata.imagery.format` | enum | 是 | The format of an imagery media item. |
| `MediaItemMetadata.imagery.dimensions` | object | 否 | The dimensions of an image. |
| `MediaItemMetadata.imagery.dimensions.width` | integer | 是 | The width of the image in pixels. |
| `MediaItemMetadata.imagery.dimensions.height` | integer | 是 | The height of the image in pixels. |
| `MediaItemMetadata.imagery.bands` | list<BandInfo> | 否 | Information about the bands of the image, if available. |
| `MediaItemMetadata.imagery.bands.BandInfo` | object | 是 | Information about a band in an image. |
| `MediaItemMetadata.imagery.bands.BandInfo.dataType` | enum | 否 | The data type of a band. |
| `MediaItemMetadata.imagery.bands.BandInfo.colorInterpretation` | enum | 否 | The color interpretation of a band. |
| `MediaItemMetadata.imagery.bands.BandInfo.paletteInterpretation` | enum | 否 | The palette interpretation of a band. |
| `MediaItemMetadata.imagery.bands.BandInfo.unitInterpretation` | object | 否 | The unit interpretation for a band. |
| `MediaItemMetadata.imagery.bands.BandInfo.unitInterpretation.unit` | string | 否 | — |
| `MediaItemMetadata.imagery.bands.BandInfo.unitInterpretation.scale` | number | 否 | — |
| `MediaItemMetadata.imagery.bands.BandInfo.unitInterpretation.offset` | number | 否 | — |
| `MediaItemMetadata.imagery.attributes` | map | 否 | The metadata attributes described in the image header in the form of a map <domain, <key, value>>.<br>For the default domain, or when the domain is not specified, the domain key will be the empty string (""). |
| `MediaItemMetadata.imagery.attributes.ImageAttributeDomain` | string | 是 | The domain of an image attribute. |
| `MediaItemMetadata.imagery.attributes.map` | map | 是 | — |
| `MediaItemMetadata.imagery.attributes.map.ImageAttributeKey` | string | 是 | The key of an image attribute within a domain. |
| `MediaItemMetadata.imagery.iccProfile` | string | 否 | The base64-encoded ICC profile for the image, if available. |
| `MediaItemMetadata.imagery.geo` | object | 否 | Embedded geo-referencing data for an image. |
| `MediaItemMetadata.imagery.geo.crs` | object | 否 | The coordinate reference system for geo-referenced imagery. |
| `MediaItemMetadata.imagery.geo.crs.wkt` | string | 否 | The Well-Known Text representation of the CRS. |
| `MediaItemMetadata.imagery.geo.geotransform` | object | 否 | An affine transformation for geo-referencing. |
| `MediaItemMetadata.imagery.geo.geotransform.xTranslate` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.geotransform.xScale` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.geotransform.xShear` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.geotransform.yTranslate` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.geotransform.yShear` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.geotransform.yScale` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.gcpInfo` | object | 否 | A list of ground control points for geo-referencing. |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps` | list<GroundControlPoint> | 否 | — |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps.GroundControlPoint` | object | 是 | A ground control point for geo-referencing. |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps.GroundControlPoint.pixX` | number | 否 | The pixel X coordinate. |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps.GroundControlPoint.pixY` | number | 否 | The pixel Y coordinate. |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps.GroundControlPoint.projX` | number | 否 | The projected X coordinate. |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps.GroundControlPoint.projY` | number | 否 | The projected Y coordinate. |
| `MediaItemMetadata.imagery.geo.gcpInfo.gcps.GroundControlPoint.projZ` | number | 否 | The projected Z coordinate. |
| `MediaItemMetadata.imagery.geo.gpsData` | object | 否 | GPS location metadata extracted from EXIF data embedded in the image. |
| `MediaItemMetadata.imagery.geo.gpsData.latitude` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.gpsData.longitude` | number | 否 | — |
| `MediaItemMetadata.imagery.geo.gpsData.altitude` | number | 否 | — |
| `MediaItemMetadata.imagery.pages` | integer | 否 | The number of pages associated with this image. Usually 1, but may be more for some formats<br>(multi-page TIFFs, for example). |
| `MediaItemMetadata.imagery.orientation` | object | 否 | The orientation information as encoded in EXIF metadata. |
| `MediaItemMetadata.imagery.orientation.rotationAngle` | enum | 否 | The rotation angle from EXIF orientation. |
| `MediaItemMetadata.imagery.orientation.flipAxis` | enum | 否 | The flip axis from EXIF orientation. |
| `MediaItemMetadata.imagery.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.spreadsheet` | object | 否 | Metadata for spreadsheet media items. |
| `MediaItemMetadata.spreadsheet.format` | enum | 是 | The format of a spreadsheet media item. |
| `MediaItemMetadata.spreadsheet.sheetNames` | list<string> | 否 | The names of the sheets in the spreadsheet. |
| `MediaItemMetadata.spreadsheet.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.spreadsheet.title` | string | 否 | The title of the spreadsheet, if available. |
| `MediaItemMetadata.spreadsheet.author` | string | 否 | The author of the spreadsheet, if available. |
| `MediaItemMetadata.untyped` | object | 否 | Metadata for untyped media items (media items without a recognized type). |
| `MediaItemMetadata.untyped.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.audio` | object | 否 | Metadata for audio media items. |
| `MediaItemMetadata.audio.format` | enum | 是 | The format of an audio media item. |
| `MediaItemMetadata.audio.specification` | object | 是 | Technical specifications for audio media items. |
| `MediaItemMetadata.audio.specification.bitRate` | integer | 是 | Approximate (average) bits per second of the audio, rounded up in case of a fractional average bits per second. |
| `MediaItemMetadata.audio.specification.durationSeconds` | number | 是 | Approximate duration of the audio, in seconds with up to two decimal digits (rounded up). |
| `MediaItemMetadata.audio.specification.numberOfChannels` | integer | 否 | Number of audio channels in the audio stream. |
| `MediaItemMetadata.audio.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.model3d` | object | 否 | Metadata for 3D model media items. |
| `MediaItemMetadata.model3d.format` | enum | 是 | The format of a 3D model media item. |
| `MediaItemMetadata.model3d.modelType` | enum | 是 | The type of 3D model representation. |
| `MediaItemMetadata.model3d.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.video` | object | 否 | Metadata for video media items. |
| `MediaItemMetadata.video.format` | enum | 是 | The format of a video media item. |
| `MediaItemMetadata.video.specification` | object | 是 | Technical specifications for video media items. |
| `MediaItemMetadata.video.specification.bitRate` | integer | 是 | Approximate (average) bits per second of the video, rounded up in case of a fractional average bits per second. |
| `MediaItemMetadata.video.specification.durationSeconds` | number | 是 | Approximate duration of the video, in seconds with up to two decimal digits (rounded up). |
| `MediaItemMetadata.video.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.dicom` | object | 否 | Metadata for DICOM (Digital Imaging and Communications in Medicine) media items. |
| `MediaItemMetadata.dicom.metaInformation` | union | 是 | DICOM meta information. |
| `MediaItemMetadata.dicom.metaInformation.v1` | object | 否 | DICOM meta information version 1. |
| `MediaItemMetadata.dicom.metaInformation.v1.mediaStorageSop` | string | 是 | The Media Storage SOP (Service-Object Pair) Class UID, which identifies<br>the type of DICOM object stored (e.g., CT Image, MR Image). |
| `MediaItemMetadata.dicom.metaInformation.v1.mediaStorageSopInstance` | string | 是 | The Media Storage SOP Instance UID. |
| `MediaItemMetadata.dicom.metaInformation.v1.transferSyntax` | string | 是 | The Transfer Syntax UID, which specifies how the DICOM data is encoded<br>(e.g., compression method, byte ordering). |
| `MediaItemMetadata.dicom.mediaType` | enum | 是 | The type of DICOM media. |
| `MediaItemMetadata.dicom.commonDataElements` | object | 是 | Common DICOM data elements. |
| `MediaItemMetadata.dicom.commonDataElements.numberFrames` | integer | 否 | The number of frames in the DICOM file. |
| `MediaItemMetadata.dicom.commonDataElements.modality` | enum | 否 | DICOM modality code. A list of modalities and their meanings can be found in the DICOM specification.<br>https://dicom.nema.org/medical/dicom/current/output/chtml/part03/sect_C.7.3.html#sect_C.7.3.1.1.1 |
| `MediaItemMetadata.dicom.commonDataElements.patientId` | string | 否 | The patient ID. |
| `MediaItemMetadata.dicom.commonDataElements.studyId` | string | 否 | The study ID. |
| `MediaItemMetadata.dicom.commonDataElements.studyUid` | string | 否 | The study UID. |
| `MediaItemMetadata.dicom.commonDataElements.seriesUid` | string | 否 | The series UID. |
| `MediaItemMetadata.dicom.commonDataElements.studyTime` | string | 否 | The study time. |
| `MediaItemMetadata.dicom.commonDataElements.seriesTime` | string | 否 | The series time. |
| `MediaItemMetadata.dicom.otherDataElements` | map | 否 | The data elements for a particular DICOM file outside of the media contained within it and the<br>data elements within the commonDataElements field. |
| `MediaItemMetadata.dicom.otherDataElements.DicomDataElementKey` | string | 是 | The key of a DICOM data element. |
| `MediaItemMetadata.dicom.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.email` | object | 否 | Metadata for email media items. |
| `MediaItemMetadata.email.format` | enum | 是 | The format of an email media item. |
| `MediaItemMetadata.email.sizeBytes` | integer | 是 | The size of the media item in bytes. |
| `MediaItemMetadata.email.sender` | list<Mailbox> | 否 | The sender(s) of the email. |
| `MediaItemMetadata.email.sender.Mailbox` | object | 是 | An email mailbox with an optional display name and email address. |
| `MediaItemMetadata.email.sender.Mailbox.displayName` | string | 否 | The display name of the mailbox. |
| `MediaItemMetadata.email.sender.Mailbox.emailAddress` | string | 是 | The email address of the mailbox. |
| `MediaItemMetadata.email.date` | string | 是 | The date the email was sent. |
| `MediaItemMetadata.email.attachmentCount` | integer | 是 | The number of attachments in the email. |
| `MediaItemMetadata.email.to` | list<MailboxOrGroup> | 否 | The recipient(s) of the email. |
| `MediaItemMetadata.email.to.MailboxOrGroup` | union | 是 | Either a mailbox or a group of mailboxes. |
| `MediaItemMetadata.email.to.MailboxOrGroup.mailbox` | object | 否 | A wrapper for a mailbox in the MailboxOrGroup union. |
| `MediaItemMetadata.email.to.MailboxOrGroup.mailbox.mailbox` | object | 是 | An email mailbox with an optional display name and email address. |
| `MediaItemMetadata.email.to.MailboxOrGroup.mailbox.mailbox.displayName` | string | 否 | The display name of the mailbox. |
| `MediaItemMetadata.email.to.MailboxOrGroup.mailbox.mailbox.emailAddress` | string | 是 | The email address of the mailbox. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group` | object | 否 | A wrapper for a group in the MailboxOrGroup union. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group.group` | object | 是 | A named group of mailboxes. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group.group.groupName` | string | 是 | The name of the group. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group.group.mailboxes` | list<Mailbox> | 否 | The mailboxes in the group. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group.group.mailboxes.Mailbox` | object | 是 | An email mailbox with an optional display name and email address. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group.group.mailboxes.Mailbox.displayName` | string | 否 | The display name of the mailbox. |
| `MediaItemMetadata.email.to.MailboxOrGroup.group.group.mailboxes.Mailbox.emailAddress` | string | 是 | The email address of the mailbox. |
| `MediaItemMetadata.email.cc` | list<MailboxOrGroup> | 否 | The CC recipient(s) of the email. |
| `MediaItemMetadata.email.cc.MailboxOrGroup` | union | 是 | Either a mailbox or a group of mailboxes. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.mailbox` | object | 否 | A wrapper for a mailbox in the MailboxOrGroup union. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.mailbox.mailbox` | object | 是 | An email mailbox with an optional display name and email address. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.mailbox.mailbox.displayName` | string | 否 | The display name of the mailbox. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.mailbox.mailbox.emailAddress` | string | 是 | The email address of the mailbox. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group` | object | 否 | A wrapper for a group in the MailboxOrGroup union. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group.group` | object | 是 | A named group of mailboxes. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group.group.groupName` | string | 是 | The name of the group. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group.group.mailboxes` | list<Mailbox> | 否 | The mailboxes in the group. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group.group.mailboxes.Mailbox` | object | 是 | An email mailbox with an optional display name and email address. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group.group.mailboxes.Mailbox.displayName` | string | 否 | The display name of the mailbox. |
| `MediaItemMetadata.email.cc.MailboxOrGroup.group.group.mailboxes.Mailbox.emailAddress` | string | 是 | The email address of the mailbox. |
| `MediaItemMetadata.email.subject` | string | 否 | The subject of the email. |
| `MediaItemMetadata.email.attachments` | list<EmailAttachment> | 否 | The attachments of the email. |
| `MediaItemMetadata.email.attachments.EmailAttachment` | object | 是 | Metadata about an email attachment. |
| `MediaItemMetadata.email.attachments.EmailAttachment.attachmentIndex` | integer | 是 | The index of the attachment in the email. |
| `MediaItemMetadata.email.attachments.EmailAttachment.fileName` | string | 否 | The file name of the attachment, if available. |
| `MediaItemMetadata.email.attachments.EmailAttachment.mimeType` | string | 是 | The verified MIME type of the attachment. |

```json
{
  "type": "imagery",
  "format": "PNG",
  "dimensions": {
    "width": 1920,
    "height": 1080
  },
  "sizeBytes": 2048576
}
```
