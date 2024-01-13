package edu.jnu.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * @author Pei Qingfu
 * @version 1.0
 * @date 2022年06月12日 19:16
 */
@RestController
@Api(tags = "oprf协议接口")
public class OprfApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(OprfApi.class);

    @GetMapping("/oprf/signature")
    @ApiOperation("获取数字x的签名接口")
    public ResponseEntity<String> getSignature(@ApiParam(value = "待签名数字", required = true) String x) {
        LOGGER.info("接收到x:"+x);
        BigInteger d = new BigInteger("23282399963902961889366589919965466215946811753019679196383601963073026613191453899926300248029233489789243362399308165690329565322928753601420278584758482746912697550351491890798946688520158669011260144232825240982614667183120321718305593611734705036861391713347126358186079249371112760227728035846980336435959248769233027730854826083718879920059961540485293082710349884551492275840955531629049426978639467925660853331260534811560188069192393264392588205879104916672550333998326293812618592325389850228055445162246436779443043223397512656518358337126551325082322672215385762922599168240697965091009692530903264807556670680484470229478940107545540835920598906558947901960121419927639247610499138992421409375496421992685721347527087138118721640418340821275729213415264535690693191336998922672043238344531550557349777887236388199934255219772830845326726984447157525409703422748423362819148906352436420831107204592249115128561378232761920339551114561374215401554512094507506349978765112270709835665263857161861822299858997294269998270864303157575035732881330471362077341228729301476533130040454166720399856834620556187156874147234515644344433865712782166068604850278956241690201025638224559129150999995816907370365944270837354719532673");
        BigInteger n = new BigInteger("531472882770570676887293000203683998395857262924991541446670888838006598797885166924231953798360109794607329237744151603917495201173382697588394565520486479897045440389197396045729909134637979342037950565164286944715296915074941318165306056611723219784320804151038181865705703854418187727984887595020393698712456038519409591918158389776622860787519923190102630707623894247248745831344027403822017518599475726034146758889175085317039374953208595391326037355868651662824427460553225467641095327491840687703263570044634178758049015580565234054421334148471889304047433287697574623716100161497681239470120121798051093986202228003217542188696754447176638745657823509746078165867784692704502582019231109600917470153996285910081586259117198962726726130547959014965905579977295977311082160426110219693228632597177359973352897897423340415941622005125866415622804824990780976808559483720287981583214783913324119411073991583196975920806996613883890633090298669766886012131202640130156013555529900867254163683268680261329440074471283510899115618237917679050056944970901403722137324559140509241664112727384991804907894028818455340243989966152105922086161015279733725729479886644300792405185458040012744056933012184889148841063842605723472941900157");
        BigInteger xBigint = new BigInteger(x);
        BigInteger y = xBigint.modPow(d, n);
        LOGGER.info("已返回签名y:"+y.toString());
        return ResponseEntity.ok(String.valueOf(y)+"n");
    }
}
