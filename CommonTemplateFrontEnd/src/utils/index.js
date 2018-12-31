export function parseTime(time, cFormat) { // 时间格式化
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

export function formatTime(time, option) { // 格式化时间
  time = +time * 1000
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

export function getEncyptSalt() { // 获取密码加密盐
  return '8b6e6521f3'
}
/* 函数防抖，减少重复触发的次数 */
export function debounce(func, wait, immediate) {
  let timeout, args, context, timestamp, result

  const later = function() {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔last小于设定时间间隔wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function(...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

export function generateTreeData(id, array) {
  const data = []
  const childArray = getChildArray(id, array)
  if (childArray.length > 0) {
    for (const item of childArray) {
      const obj = {}
      obj.id = item.id
      obj.label = item.name
      const children = generateTreeData(item.id, array)
      obj.children = children
      data.push(obj)
    }
    return data
  }
}

function getChildArray(id, array) {
  const arr = []
  for (const item of array) {
    if (item.parentId === id) {
      arr.push(item)
    }
  }
  return arr
}

export function filterSelectNodeId(treeData, ids) {
  let data = []
  for (const item of treeData) {
    if (typeof (item.children) !== 'undefined' && item.children.length > 0) {
      data = data.concat(filterSelectNodeId(item.children, ids))
    } else {
      if (ids.includes(item.id)) {
        data.push(item.id)
      }
    }
  }
  return data
}

export function filterSelectNode(treeData, ids) {
  let data = []
  for (const item of treeData) {
    if (typeof (item.children) !== 'undefined' && item.children.length > 0) {
      data = data.concat(filterSelectNode(item.children, ids))
    } else {
      if (ids.includes(item.id)) {
        data.push(item)
      }
    }
  }
  return data
}
