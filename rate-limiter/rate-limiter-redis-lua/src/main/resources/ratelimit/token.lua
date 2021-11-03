-- key
local key = "ratelimit:"..KEYS[1]
-- 最大存储的令牌数
local max_permits = tonumber(KEYS[2])
-- 每分钟产生的令牌数
local permits_per_min = tonumber(KEYS[3])
-- 当前时间
local now_micros = tonumber(ARGV[1])
-- 请求的令牌数
local required_permits = tonumber(ARGV[2])

-- 下次请求可以获取令牌的起始时间
local next_free_ticket_micros = tonumber(redis.call('hget', key, 'next_free_ticket_micros') or 0)

-- redis.log(-- redis.log_NOTICE,'now_micros:'..tostring(now_micros))
-- redis.log(-- redis.log_NOTICE,'next_free_ticket_micros1:'..tostring(next_free_ticket_micros))

-- 查询获取令牌是否超时
if (ARGV[3] ~= nil) then
    -- 获取令牌的超时时间
    local timeout_micros = tonumber(ARGV[3])
    local micros_to_wait = next_free_ticket_micros - now_micros
    if (micros_to_wait > timeout_micros) then
        return micros_to_wait
    end
end

-- 当前存储的令牌数
local stored_permits = tonumber(redis.call('hget', key, 'stored_permits') or 0)

-- redis.log(-- redis.log_NOTICE,'stored_permits1:'..tostring(stored_permits))
-- 添加令牌的时间间隔(毫秒)
local stable_interval_micros = 60000 / permits_per_min

-- 补充令牌
if (now_micros > next_free_ticket_micros) then
    local new_permits,tmp = math.modf((now_micros - next_free_ticket_micros) / stable_interval_micros)
    
	-- redis.log(-- redis.log_NOTICE,'new_permits:'..tostring(new_permits))
    stored_permits = math.min(max_permits, stored_permits + new_permits)
	-- redis.log(-- redis.log_NOTICE,'stored_permits2:'..tostring(stored_permits))
    next_free_ticket_micros = now_micros
	-- redis.log(-- redis.log_NOTICE,'next_free_ticket_micros3:'..tostring(next_free_ticket_micros))
end

-- 消耗令牌
local moment_available = next_free_ticket_micros
local stored_permits_to_spend = math.min(required_permits, stored_permits)

-- redis.log(-- redis.log_NOTICE,'stored_permits_to_spend:'..tostring(stored_permits_to_spend))
local fresh_permits = required_permits - stored_permits_to_spend
local wait_micros = fresh_permits * stable_interval_micros

-- redis.log(-- redis.log_NOTICE,'wait_micros:'..tostring(wait_micros))

redis.call('hset', key, 'stored_permits', stored_permits - stored_permits_to_spend)
redis.call('hset', key, 'next_free_ticket_micros', next_free_ticket_micros + wait_micros)
redis.call('expire', key, 60)

-- 返回需要等待的时间长度
return moment_available - now_micros