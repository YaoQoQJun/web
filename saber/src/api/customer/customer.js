import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/blade-customer/customer/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/blade-customer/customer/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/blade-customer/customer/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/blade-customer/customer/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/blade-customer/customer/save',
    method: 'post',
    data: row
  })
}


//导出设备列表
export const exportCustomer = ids => {
  return request({
    url: "/api/blade-customer/customer/exportCustomer",
    method: "post",
    responseType: "arraybuffer",
    params: {
      ids:ids
    }
  });
};
