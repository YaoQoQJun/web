import request from "@/router/axios";

export const getList = (current, size, params) => {
  return request({
    url: "/api/blade-customer/customer/page",
    method: "get",
    params: {
      ...params,
      current,
      size
    }
  });
};

//单个删除图片
export const removeCustomerImg = imgName => {
  return request({
    url: "/api/blade-customer/customer/removeCustomerImg",
    method: "post",
    params: {
      imgName: imgName
    }
  });
};

//单个保存图片
export const saveCustomerImg = row => {
  return request({
    url: "/api/blade-customer/customer/saveCustomerImg",
    method: "post",
    data: row
  });
};

export const getDetail = id => {
  return request({
    url: "/api/blade-customer/customer/detail",
    method: "get",
    params: {
      id
    }
  });
};

export const remove = ids => {
  return request({
    url: "/api/blade-customer/customer/remove",
    method: "post",
    params: {
      ids
    }
  });
};

export const add = row => {
  return request({
    url: "/api/blade-customer/customer/save",
    method: "post",
    data: row
  });
};

export const update = row => {
  return request({
    url: "/api/blade-customer/customer/update",
    method: "post",
    data: row
  });
};

export const chenkPhone = row => {
  return request({
    url: "/api/blade-customer/customer/chenkPhone",
    method: "post",
    data: row
  });
};

//导出设备列表
export const exportCustomer = ids => {
  return request({
    url: "/api/blade-customer/customer/exportCustomer",
    method: "post",
    responseType: "arraybuffer",
    params: {
      ids: ids
    }
  });
};

export const allCount = () => {
  return request({
    url: "/api/blade-customer/customerCount/allCount",
    method: "post"
  });
};

export const personalCount = params => {
  return request({
    url: "/api/blade-customer/customerCount/personalCount",
    method: "post",
    params: params
  });
};
