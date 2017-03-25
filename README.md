# Moqui Captcha Addon

This [Moqui](https://github.com/moqui/moqui-framework) add-on component adds support of captcha to Moqui. 

After startup, `ImageCaptchaServlet` processes all request to `/jcaptcha` which generate a captcha image.

## How to use

### The form 

Provide a form containing the captcha challenge (the image) and an input text for the response

```
<img id="captcha" src="/jcaptcha">
<input type='text' name='j_captcha_response' value=''>
```

### Server side 

```
import com.moshi.impl.captcha.CaptchaServiceSingleton
boolean isResponseCorrect = CaptchaServiceSingleton.validateResponseForID(ec, j_captcha_response)
```

You may change `j_captcha_response` to whatever name you prefer

### Tips

You may add a dummy query string to `/jcaptcha` like 

```
$('#captcha').attr('src', '/jcaptcha?t=' + new Date().getTime())
```
to refresh captcha image.


## Example

![Captcha](captcha.png)

## References:

https://jcaptcha.atlassian.net/wiki/display/general/5+minutes+application+integration+tutorial 


# License

Moqui Captcha is [CC0-licensed](./LICENSE.md). we also provide an addition [copyright and patent grant](./AUTHORS) 