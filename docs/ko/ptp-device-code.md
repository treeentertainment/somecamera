# 📸 Canon & Nikon PTP/IP 카메라 명령어 레퍼런스

이 문서는 **PTP (Picture Transfer Protocol) / PTP/IP 명령 코드** 중 Canon 및 Nikon 카메라에서 사용되는 항목을 정리한 것입니다.  
각 코드(작동, 이벤트, 디바이스 속성, 이미지 포맷 등)에 대한 상세 정보는 [PTP 공식 문서](https://en.wikipedia.org/wiki/Picture_Transfer_Protocol) 및 Canon/Nikon SDK 자료를 참고하세요.

---

## 📷 Canon EOS 카메라

### **지원되는 동작 (Operation Codes)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x1002    | OpenSession |
| 0x1003    | CloseSession |
| 0x1004    | GetStorageIDs |
| 0x1005    | GetStorageInfo |
| 0x1006    | GetNumObjects |
| 0x1007    | GetObjectHandles |
| 0x1008    | GetObjectInfo |
| 0x1009    | GetObject |
| 0x100a    | GetThumb |
| 0x100b    | DeleteObject |
| 0x100c    | SendObjectInfo |
| 0x100d    | SendObject |
| 0x100f    | FormatStore |
| 0x1014    | GetDevicePropDesc |
| 0x1015    | GetDevicePropValue |
| 0x1016    | SetDevicePropValue |
| 0x101b    | GetPartialObject |
| 0x9101    | EosGetStorageIds |
| 0x9102    | EosGetStorageInfo |
| 0x9103    | EosUnknown_GetObjectInfo |
| 0x9104    | EosUnknown_GetObject |
| 0x9105    | EosUnknown_SetObject |
| 0x9106    | EosUnknown_DeleteObject |
| 0x9107    | EosUnknown_GetPartialObject |
| 0x9108    | EosGetDeviceInfoEx |
| 0x9109    | EosGetObjectIds |
| 0x910a    | EosUnknown_GetThumbEx |
| 0x910b    | EosUnknown_SendPartialObject |
| 0x910c    | EosUnknown_SetObjectAttributes |
| 0x910e    | EosUnknown_SetObjectTime |
| 0x910f    | EosTakePicture |
| 0x9110    | EosSetDevicePropValue |
| 0x9113    | EosGetPCConnectMode |
| 0x9114    | EosSetPCConnectMode |
| 0x9115    | EosSetEventMode |
| 0x9116    | EosEventCheck |
| 0x9117    | EosTransferComplete |
| 0x9118    | EosCancelTransfer |
| 0x911b    | EosUnknown_SetUILock |
| 0x911c    | EosUnknown_ResetUILock |
| 0x911d    | EosUnknown_KeepDeviceOn |
| 0x911f    | EosUnknown_UpdateFirmware |
| 0x9120    | EosUnknown_TransferCompleteDT |
| 0x9121    | EosUnknown_CancelTransferDT |
| 0x9125    | EosBulbStart |
| 0x9126    | EosBulbEnd |
| 0x9127    | EosGetDevicePropValue |
| 0x9153    | EosGetLiveViewPicture |
| 0x9154    | EosDoAf |
| 0x9155    | EosDriveLens |
| 0x9157    | EosUnknown_ClickWB |
| 0x9158    | EosUnknown_Zoom |
| 0x9159    | EosUnknown_ZoomPosition |
| 0x9128    | EosRemoteReleaseOn |
| 0x9129    | EosRemoteReleaseOff |
| 0x9131    | EosUnknown_PopupBuiltinFlash |

### **지원되는 이벤트 (Event Codes)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x4002    | ObjectAdded |
| 0x4003    | ObjectRemoved |
| 0x4004    | StoreAdded |
| 0x4005    | StoreRemoved |
| 0x4007    | ObjectInfoChanged |
| 0x4009    | RequestObjectTransfer |
| 0xc101    | NikonObjectAddedInSdram |

### **지원되는 디바이스 속성 (Device Property Codes)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0xd402    | MtpDeviceFriendlyName |
| 0xd406    | MtpSessionInitiatorInfo |
| 0xd407    | MtpPerceivedDeviceType |
| 0x5001    | BatteryLevel |

### **캡처 포맷 (CaptureFormats)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x3801    | EXIF_JPEG |

### **이미지 포맷 (ImageFormats)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x3001    | Association |
| 0x3002    | Script |
| 0x3006    | DPOF |
| 0x300a    | AVI |
| 0x3008    | WAV |
| 0x3801    | EXIF_JPEG |
| 0xb101    | EosCRW |
| 0xb103    | EosCRW3 |
| 0xb104    | EosMOV |
| 0xbf02    | Unknown |
| 0x3800    | UnknownImageObject |

---

## 📷 Nikon 카메라

### **지원되는 동작 (Operation Codes)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x1001    | GetDeviceInfo |
| 0x1002    | OpenSession |
| 0x1003    | CloseSession |
| 0x1004    | GetStorageIDs |
| 0x1005    | GetStorageInfo |
| 0x1006    | GetNumObjects |
| 0x1007    | GetObjectHandles |
| 0x1008    | GetObjectInfo |
| 0x1009    | GetObject |
| 0x100a    | GetThumb |
| 0x100b    | DeleteObject |
| 0x100c    | SendObjectInfo |
| 0x100d    | SendObject |
| 0x100e    | InitiateCapture |
| 0x100f    | FormatStore |
| 0x1014    | GetDevicePropDesc |
| 0x1015    | GetDevicePropValue |
| 0x1016    | SetDevicePropValue |
| 0x101b    | GetPartialObject |
| 0x90c0    | NikonInitiateCaptureRecInSdram |
| 0x90c1    | NikonAfDrive |
| 0x90c2    | NikonChangeCameraMode |
| 0x90c3    | NikonDeleteImagesInSdram |
| 0x90c4    | NikonGetLargeThumb |
| 0x90c5    | Unknown |
| 0x90c6    | Unknown |
| 0x90c7    | NikonGetEvent |
| 0x90c8    | NikonDeviceReady |
| 0x90c9    | NikonSetPreWbData |
| 0x90ca    | NikonGetVendorPropCodes |
| 0x90cb    | NikonAfAndCaptureInSdram |
| 0x9801    | NikonGetObjectPropsSupported |
| 0x9802    | NikonGetObjectPropDesc |
| 0x9803    | NikonGetObjectPropValue |
| 0x9805    | NikonGetObjectPropList |

### **지원되는 이벤트 (Event Codes)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x4001    | CancelTransaction |
| 0x4002    | ObjectAdded |
| 0x4004    | StoreAdded |
| 0x4005    | StoreRemoved |
| 0x4006    | DevicePropChanged |
| 0x4008    | DeviceInfoChanged |
| 0x4009    | RequestObjectTransfer |
| 0x400a    | StoreFull |
| 0x400c    | StorageInfoChanged |
| 0x400d    | CaptureComplete |
| 0xc101    | NikonObjectAddedInSdram |
| 0xc102    | NikonCaptureCompleteRecInSdram |

### **지원되는 디바이스 속성 (Device Property Codes)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x5001    | BatteryLevel |
| 0x5003    | ImageSize |
| 0x5004    | CompressionSetting |
| 0x5005    | WhiteBalance |
| 0x5007    | FNumber |
| 0x5008    | FocalLength |
| 0x500a    | FocusMode |
| 0x500b    | ExposureMeteringMode |
| 0x500c    | FlashMode |
| 0x500d    | ExposureTime |
| 0x500e    | ExposureProgramMode |
| 0x500f    | ExposureIndex |
| 0x5010    | ExposureBiasCompensation |
| 0x5011    | DateTime |
| 0x5013    | StillCaptureMode |
| 0x5018    | BurstNumber |
| 0x501c    | FocusMeteringMode |
| 0xd406    | MtpSessionInitiatorInfo |
| 0xd407    | MtpPerceivedDeviceType |

### **캡처 포맷 (CaptureFormats)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x3801    | EXIF_JPEG |
| 0x3000    | UnknownNonImageObject |

### **이미지 포맷 (ImageFormats)**
| 코드 (hex) | 설명 |
|-----------|------|
| 0x3000    | UnknownNonImageObject |
| 0x3001    | Association |
| 0x3002    | Script |
| 0x3006    | DPOF |
| 0x300a    | AVI |
| 0x3008    | WAV |
| 0x3801    | EXIF_JPEG |
| 0xb101    | NikonNEF |
| 0xb103    | NikonMOV |
| 0xbf02    | Unknown |

---

**참고 링크**

- [PTP - 위키백과](https://en.wikipedia.org/wiki/Picture_Transfer_Protocol)  
- [Canon EOS Digital SDK](https://developer.canon/)  
- [Nikon SDK / Camera Control](https://sdk.nikonimaging.com/)  
