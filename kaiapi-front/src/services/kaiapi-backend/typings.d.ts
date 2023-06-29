declare namespace API {
  type AddUserInterfaceTo = {
    interfaceId?: number;
    orderNum?: number;
    userId?: number;
  };

  type BaseResponse = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseboolean = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseInterfaceDetailVo = {
    code?: number;
    data?: InterfaceDetailVo;
    message?: string;
  };

  type BaseResponseInterfaceInfoVo = {
    code?: number;
    data?: InterfaceInfoVo;
    message?: string;
  };

  type BaseResponseListInterfaceInfo = {
    code?: number;
    data?: InterfaceInfo[];
    message?: string;
  };

  type BaseResponseListUserInterfaceInfo = {
    code?: number;
    data?: UserInterfaceInfo[];
    message?: string;
  };

  type BaseResponseListUserVO = {
    code?: number;
    data?: UserVO[];
    message?: string;
  };

  type BaseResponseLoginUserVo = {
    code?: number;
    data?: LoginUserVo;
    message?: string;
  };

  type BaseResponselong = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponsePageInterfaceInfoVo = {
    code?: number;
    data?: PageInterfaceInfoVo;
    message?: string;
  };

  type BaseResponsePageInterfaceLeftVo = {
    code?: number;
    data?: PageInterfaceLeftVo;
    message?: string;
  };

  type BaseResponsePageManageInterfaceVo = {
    code?: number;
    data?: PageManageInterfaceVo;
    message?: string;
  };

  type BaseResponsePageUserInterfaceInfoVO = {
    code?: number;
    data?: PageUserInterfaceInfoVO;
    message?: string;
  };

  type BaseResponsePageUserVO = {
    code?: number;
    data?: PageUserVO;
    message?: string;
  };

  type BaseResponsestring = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUserInterfaceInfo = {
    code?: number;
    data?: UserInterfaceInfo;
    message?: string;
  };

  type BaseResponseUserVO = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type bindPhoneUsingPOSTParams = {
    captcha?: string;
    code?: string;
    id?: number;
    mobile?: string;
    userAccount?: string;
  };

  type BranchDeleteRequest = {
    ids?: number[];
  };

  type DeleteRequest = {
    id?: number;
  };

  type feignGetInterfaceInfoByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getInterfaceInfoByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserInterfaceInfoByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type IdRequest = {
    id?: number;
  };

  type InterfaceDetailVo = {
    description?: string;
    id?: number;
    isFree?: number;
    leftNum?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParams?: string;
    responseHeader?: string;
    status?: number;
    url?: string;
  };

  type InterfaceInfo = {
    charging?: number;
    createTime?: string;
    description?: string;
    id?: number;
    isDelete?: number;
    isFree?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParams?: string;
    responseHeader?: string;
    status?: number;
    updateTime?: string;
    url?: string;
    userId?: number;
  };

  type InterfaceInfoAddRequest = {
    charging?: number;
    description?: string;
    isFree?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParams?: string;
    responseHeader?: string;
    url?: string;
  };

  type InterfaceInfoUpdateRequest = {
    charging?: number;
    description?: string;
    id?: number;
    isFree?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParams?: string;
    responseHeader?: string;
    status?: number;
    url?: string;
  };

  type InterfaceInfoVo = {
    charging?: number;
    description?: string;
    id?: number;
    isFree?: number;
    method?: string;
    name?: string;
    url?: string;
  };

  type InterfaceInvokeRequest = {
    id?: number;
    userRequestParams?: string;
  };

  type InterfaceLeftVo = {
    description?: string;
    id?: number;
    leftNum?: number;
    method?: string;
    name?: string;
    url?: string;
  };

  type listInterfaceInfoAllByPageUsingGETParams = {
    current?: number;
    description?: string;
    id?: number;
    method?: string;
    name?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type listInterfaceInfoByPageUsingGETParams = {
    /** current */
    current?: number;
    /** size */
    size?: number;
  };

  type listInterfaceInfoUsingGETParams = {
    current?: number;
    description?: string;
    id?: number;
    method?: string;
    name?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type listUserByPageUsingGETParams = {
    createTime?: string;
    current?: number;
    gender?: number;
    id?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type listUserInterfaceInfoByPageUsingGETParams = {
    current?: number;
    id?: number;
    interfaceName?: string;
    leftNum?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    totalNum?: number;
    userAccount?: string;
  };

  type listUserInterfaceInfoUsingGETParams = {
    current?: number;
    id?: number;
    interfaceName?: string;
    leftNum?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    totalNum?: number;
    userAccount?: string;
  };

  type listUserInterfaceLeftNumByPageUsingGETParams = {
    /** current */
    current?: number;
    /** size */
    size?: number;
  };

  type listUserUsingGETParams = {
    createTime?: string;
    current?: number;
    gender?: number;
    id?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type LoginUserVo = {
    gender?: number;
    id?: number;
    mobile?: string;
    token?: string;
    tokenHead?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type ManageInterfaceVo = {
    charging?: number;
    createTime?: string;
    description?: string;
    id?: number;
    isDelete?: number;
    isFree?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParams?: string;
    responseHeader?: string;
    status?: number;
    updateTime?: string;
    url?: string;
    userId?: number;
  };

  type ModelAndView = {
    empty?: boolean;
    model?: Record<string, any>;
    modelMap?: Record<string, any>;
    reference?: boolean;
    status?:
      | 'ACCEPTED'
      | 'ALREADY_REPORTED'
      | 'BAD_GATEWAY'
      | 'BAD_REQUEST'
      | 'BANDWIDTH_LIMIT_EXCEEDED'
      | 'CHECKPOINT'
      | 'CONFLICT'
      | 'CONTINUE'
      | 'CREATED'
      | 'DESTINATION_LOCKED'
      | 'EXPECTATION_FAILED'
      | 'FAILED_DEPENDENCY'
      | 'FORBIDDEN'
      | 'FOUND'
      | 'GATEWAY_TIMEOUT'
      | 'GONE'
      | 'HTTP_VERSION_NOT_SUPPORTED'
      | 'IM_USED'
      | 'INSUFFICIENT_SPACE_ON_RESOURCE'
      | 'INSUFFICIENT_STORAGE'
      | 'INTERNAL_SERVER_ERROR'
      | 'I_AM_A_TEAPOT'
      | 'LENGTH_REQUIRED'
      | 'LOCKED'
      | 'LOOP_DETECTED'
      | 'METHOD_FAILURE'
      | 'METHOD_NOT_ALLOWED'
      | 'MOVED_PERMANENTLY'
      | 'MOVED_TEMPORARILY'
      | 'MULTIPLE_CHOICES'
      | 'MULTI_STATUS'
      | 'NETWORK_AUTHENTICATION_REQUIRED'
      | 'NON_AUTHORITATIVE_INFORMATION'
      | 'NOT_ACCEPTABLE'
      | 'NOT_EXTENDED'
      | 'NOT_FOUND'
      | 'NOT_IMPLEMENTED'
      | 'NOT_MODIFIED'
      | 'NO_CONTENT'
      | 'OK'
      | 'PARTIAL_CONTENT'
      | 'PAYLOAD_TOO_LARGE'
      | 'PAYMENT_REQUIRED'
      | 'PERMANENT_REDIRECT'
      | 'PRECONDITION_FAILED'
      | 'PRECONDITION_REQUIRED'
      | 'PROCESSING'
      | 'PROXY_AUTHENTICATION_REQUIRED'
      | 'REQUESTED_RANGE_NOT_SATISFIABLE'
      | 'REQUEST_ENTITY_TOO_LARGE'
      | 'REQUEST_HEADER_FIELDS_TOO_LARGE'
      | 'REQUEST_TIMEOUT'
      | 'REQUEST_URI_TOO_LONG'
      | 'RESET_CONTENT'
      | 'SEE_OTHER'
      | 'SERVICE_UNAVAILABLE'
      | 'SWITCHING_PROTOCOLS'
      | 'TEMPORARY_REDIRECT'
      | 'TOO_EARLY'
      | 'TOO_MANY_REQUESTS'
      | 'UNAUTHORIZED'
      | 'UNAVAILABLE_FOR_LEGAL_REASONS'
      | 'UNPROCESSABLE_ENTITY'
      | 'UNSUPPORTED_MEDIA_TYPE'
      | 'UPGRADE_REQUIRED'
      | 'URI_TOO_LONG'
      | 'USE_PROXY'
      | 'VARIANT_ALSO_NEGOTIATES';
    view?: View;
    viewName?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageInterfaceInfoVo = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: InterfaceInfoVo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageInterfaceLeftVo = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: InterfaceLeftVo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageManageInterfaceVo = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: ManageInterfaceVo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserInterfaceInfoVO = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserInterfaceInfoVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserVO = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type sendSmsUsingGETParams = {
    /** phoneNum */
    phoneNum?: string;
  };

  type UserInterfaceInfo = {
    createTime?: string;
    id?: number;
    interfaceInfoId?: number;
    isDelete?: number;
    leftNum?: number;
    totalNum?: number;
    updateTime?: string;
    userId?: number;
  };

  type UserInterfaceInfoAddRequest = {
    interfaceName?: string;
    leftNum?: number;
    userAccount?: string;
  };

  type UserInterfaceInfoUpdateRequest = {
    id?: number;
    leftNum?: number;
  };

  type UserInterfaceInfoVO = {
    id?: number;
    interfaceDescription?: string;
    interfaceInfoId?: number;
    interfaceName?: string;
    leftNum?: number;
    totalNum?: number;
    userAccount?: string;
    userId?: number;
    userName?: string;
  };

  type UserLoginBySmsRequest = {
    code?: string;
    mobile?: string;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserRegisterRequest = {
    captcha?: string;
    checkPassword?: string;
    code?: string;
    mobile?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserUpdateRequest = {
    gender?: number;
    id?: number;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
  };

  type UserVO = {
    accessKey?: string;
    createTime?: string;
    gender?: number;
    id?: number;
    secretKey?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type View = {
    contentType?: string;
  };
}
