---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by ZhangXiaoHua.
--- DateTime: 2023-2-28 下午 11:33
if redis.call("get", KEYS[1]) == ARGV[1] then
    if redis.call("del", KEYS[1]) == 1 then
        return true;
    else
        return false;
    end
else
    return false;
end