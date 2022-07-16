package com.zjy.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeTypeUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.regex.Matcher;

@Slf4j
public class FileUtils {
    private static MimetypesFileTypeMap map = new MimetypesFileTypeMap();

    static {
        map.addMimeTypes("application/javascript js");
        map.addMimeTypes("application/x-chrome-extension crx");
        map.addMimeTypes("application/java-archive jar");
        map.addMimeTypes("audio/x-pn-realaudio rmvb");
    }

    public static String getMimeType(String file) {
        if (file == null || "".equals(file.trim())) return MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;
        return getMimeType(new File(file));
    }

    public static String getMimeType(File file) {
        String mimeType = null;
        try {
            mimeType = Files.probeContentType(file.toPath());
        } catch (IOException ignored) {
        }
        if (mimeType != null && !"".equals(mimeType)) return mimeType;

        mimeType = map.getContentType(file);
        if (mimeType != null && !"".equals(mimeType)) return mimeType;

//        ConfigurableMimeFileTypeMap mimeMap = new ConfigurableMimeFileTypeMap();
//        return mimeMap.getContentType(file);

//        Collection mimeTypes = MimeUtil.getMimeTypes(file);
//        if(mimeTypes != null && mimeTypes.size() > 0) {
//            mimeType = mimeTypes.toString();
//        }
        return mimeType;
    }

    public static void main(String[] args) {
        String[] s = new String[]{".txt", ".html", ".css", ".json", ".xml", ".svg", ".zip", ".gz", ".tar",
                ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".xls", ".xlsx", ".doc", ".docx", ".ppt", ".pptx", ".csv", ".pdf",
                ".wma", ".wmv", ".ogg", ".aac", ".mkv", ".mp3", ".mp4", ".wav", ".avi", ".m4a", ".m3u", ".3gp", ".exe",
                ".js", ".crx", ".jar", ".rmvb",
                ".rar", "abc", ".bin", ".woff", ".7z", ".msg", ".mpg4", ".m4u", ".bat", ".flv", ".log"};
        for (String s1 : s) {
            String mimeType = getMimeType(s1);
            System.out.println(s1 + ": " + mimeType);
        }

        String base64 = "data:image/webp;base64,UklGRkYlAABXRUJQVlA4IDolAACwpwCdASpHAUcBPp1EnUulo6KspzGriZATiWNu/DGu42EkYfx/8N6a1xf0vGt2f5s/S/m0/5XrM/QvpA9RXzl+aBeLnrLY/rNN5d/pvGHzFe1v3D90Pkh/Oc8dqX87/C37/+7+3D+675fkX/o+oR7J/z/AXgE/Pv7V/yf7r7Lf4/nF9pf+x7gX5rcfb617BH549YT/G/+P+49PH1J/7P9T8DX8//vP/TL5nTlf1wb42AlBRSFRRhsTEz24FWA0FJlb6p6Ckjr6QpUvSNr6eaH/0PUxw134Dx6+nTmK1KY0dXnv+53NzZfOyc888JC3hr2lx5uaRZncD/DhOMXg+JwU3IXpt09nB2lZ30eUZ5X//3g/78BhA0x/fDg+zpVCt5pmnKjF8ce4bMgTMdrPDvuY1r9fQeppFw5Z6QxruaUeUB4MXKccaJfA6536/jF8mBwYI1DHKzwyi/Y+480/jSEn0jpPvjq1E6albNvVcmJG3Ce/rXIV8eJH21TUBGi0pVYMEfNJdJo412/vR496wI2XHdnkk0VvD1176AL5eoP+F9NOZx1sd1Bp/iF5EOMpXBLjPVdAlqWmmqRQmatsofqvSiV+Oc85d8khRqrW9vugZUhyTYHvJRQMCsDZvRSOPGmATJt7+I7JpgSz7Ca5mH9lSwe4ynburBCTEZP01iX1W3p9JoR2mGxj7s/ogP4I2kQKCtSNr031i2vhD3C5QcEJewui8b8EAHfS8rigk+TTInm1yeOa+G1HaPwDPJM7cRzeJ275xNmhHCBWBBeRSfmYZm3kXo58UGIydL1qrsMAxOQEk5jqI/6hNQBfVjwwrA7nVuIM/Q8PfShDFdxLl6mTJjK3w4TkrU+FHfWbgx3W8XlLiX0pV5zZPoAVabQx9gLaP5AyCcOpDpm4+Dwymch1I5fl5b+ny1ybbT0NjRDwtyQpIa1l0RPKRl6ZP4TDCaqrcaPZuY3GsyL3b7KwMti58Yvv253Pj5vMcBx/wu8cGkVoeFe3ASE2bVYqRlf+C1isTSzXPVk07EHwg6TA//7IaXwgWiGwEnfNuqbbU68Q5is4Zhc/aK7Z02ID+RlMy5v5ct2T4hbp0/Rzq3ILlJhh3ZcE9EUjxTa7TcNF735mYWWetmUs7QOWML//FQ68jd1I57YU+qQNUQOmmLFhRPa3kp8vwaxu/7ZAW460AAIw/qhG5/yAV6nCu6QlrzadOoxH8vWdp/sLbGQL4UOJipZwjk4ThJGvja/zyOLW9MuGwN1YxPhR66ZbamIs7Fe2Ct4ad8heavVQfeEUFLYHBjzI5cFJkdJmStLWI1Qk5jzT2EcFUUS7P0DG0IDamRGtOzaJrN0/jnmxk06wmNW9oD6AW3Iunv7V5yAwS2MsfMucPmmgqaEeHqYHnS8yj8xZUH5LGXbzHAJ164O4HnmF+Zb6zZuGeWAkj3jJxhT4oAs2jKUosm/i1DsjjuCKjeEgRbjFkns8r75wLxH04RIvMEKTjyWRI6+jy9211xUVtIF+NX7gu1nTaPKaH+3e3lSvDov3J4wE3Rle2M/1WEuCpxAuOJ5+JkYjLPnXw5F+BuZnZn55JYQECdgqGi1YexJZkM5s+WS2ckdiDyCEzp4oKZ1lUiesEFmyujV+v09JIqFalMKupEJ3drmwN/XQA6lnnP0PCRtKAVkWXaGkQwjTYr2d8yhrjdkwKMJodaguEBlV7uIezJLPpo6+nLqtWK/psSiwn9rYxSdcjMglHYg4m7l1m94KWu6ngdZTr+Y/s0LX+6wPFalMyeyVCRNCsiKWQ7rPnX06YwAA/vq2gD3fb4dMPP8/lcayzuM+y5TGEyw6ppDiL1TnInkLMMf0CbONd+FdrFYhrCMktC68pf29q/BZiRU/Yu6t52bgcgHSOlpLtHWZG4VZz0AYouuBOSF9h5ofmtwumRlD4oQ4jKQPqwLJMgZHNxq9R79NZ5h17qXvvxHrhze4l+FnecxMpsV1rpE+43GMi08qVtcUHaXjpX5ALH43ifpccyH0Xg+t7cEZwD/3rSUeoyqC+9KnaJLQEc1eJQExxw832nAoEDOMRCatDM4dsFan93WDoxcfWqv/+sRoPMSmRgR7WADrETcjjr0fCnjSHOJHKCUPIVpMhdxrwc7P4y+LzbrQ/eMZC/j5oA86KgAAAAAAt+vB9nZSiGKHsdgdOTcFRuZp3HQR/V9a+qqZ6TWFW9OGmjsFBCx6etizJCiJv+7NHxYCL/euLxgSf5QoYXfAIpR8XqPRS3mBO/48Z4yNr9aS2OvWfBA3PvjVCf9RIwNZisAfAZ1qhXLZgaHwAamczcdEW4G6iOh6b9h3I9mJzykeA8bm5/v4HFYFA78sTCzvfl5BQbKPaD5EHUdBUknqNLtCPklkQj6Enueo7oClk/mQsepc0cunBcqmIByQAYJQpuf2Axaqa5A/jwxMTacHW3dtwVa7UScrZppB9ysoBVt/3hDHfqHhfhPvvWBtIV34oabJfcearGp5vugiFWBaO2m61UKlzpQ57F0u1Kt68EYF/27tWu/nqT2z3aAABXPPRc1tc4CuSPkq9Q5qUo+jPIWjZi0vUche5eUbOgJl/gwgbFb2ZqOUIC7S5efPk2B3Xn/HI68qxmZI+M2eLn9tKy+fyWwP6xzEm7IugEHBQ5qtgYjGuzvwAsbd5ktshpFuDB4rrsWKAEoNwQRlGql5zPkJz5BkHwMjrXT97iFnl5WE0kKTL5f9+q0pajbTZcAgZTPQxeRn56ZbBdGpLuVY5pgkyRdfGyJh5boQa7IcvT9pglw+cOnsQ4naZtigoBq6Lj4qAGyoEk+mHi+uBHviPutztT5hSS0iujBvyngiaHq1hCc/IDJMaS31rq4byrJIUJLkSFPxD/j8dPtV5Ai7eOBduyWEU0uYdeny4yIpSV3Gm/iWLXN6qy1sthBqjiaslGvlzqaTTu/O8N2uB9rCIK9hnbzYhlImQG9odblUWXgnQduuTWka3KtSAPJ7c7Xbt+si1CaG5LbalLEcz+nYYyqtYLf9kCV6Sl/EXGeB3SVwQ3wDD+BUQT4GHn66pQLisPuwO9wLXVqqmBwaqW/6zsxgDMBEf/OSBh9mu0wfTLVjvAXwKqoy0E7pcar8Hp5fvV+9A3+UeMr9sGJn0nYMDwD5lTVtnL2KVrt3wjNkYWnBHtmgW9+9l89UkeW0WPJX/pHHB/jSZIKMwN+iOP4MZWvaYOCylhDZolT4+l/s29KtJpXq0wVRaKhSemqh6Asx3tv56UwjvkgHpqvbP5NjbkyU4ZMKiFsR8geCi0n0F1m16lxplNKdzU9HtaDXsPbY2O22+uB65jZuw/nI16Iy/gdiiAE3kEk3YVcvnXYI7UCuXgsnt0NBDvcjZ9kzv9Jqaa0TqR3x/20HP/XtQVkLyI6bG5iZ01E3XqZsxXPMaXOeC+B7IDgRp0juEajS+AhvisjbUZkMSy9pRrBpzlCEKjDrdMtR2X7pw2aoveP/FeZdjnF/nRzzC5xhZ4o1OJfXvcdLuVQj4BojhbBtqvnqFOJJRw7Ic3yuE1J1rAKc6I6r3T3/axC83lhyhZO4Rs079ehV5jtGbyBcZeH5FjG8b28QQnFPP79PL3jHvyXOIaNOetlmKJv5fYt55EXKPLayIa3nsdELFK8o17ewgTEptYQc5kiUPlLb3LqVC57ejaf7sf9ChtfJUYf5/Z7vCUwGTTKXcmp8htjHtjmf/4OydbWRTQYOJb2/a6IJaGdP0hdj+Ocb2fmBAZA7cybOJ+XnT2nm4OxUKbrFqM6VcGOEUy3nrYtAuQHGP/ShfeFSrwrGfTUo6YAYHjT5HvSQe1nhEsrXoHoFBbKkOHSTupSgiF7odIq51iZqaQlcMJq4tCUM/1IdBzC3RvnTeFLqxvf0dtBO1NoRX1HEoNvXQZuLR8sI9D2vNwzx5Mqu3nqE36F8e+qXiTyuMdbYyeIRjAxIsYnoIY0zXtnPd2UMVNfdT3c8UTEUUTG6VYCu1wo4icsfY3kMFfyyjBJbnIryB0GAF5UAv8KDg6gS7nkv2pbUWdpQcnpcrBW/Y1LaSj5VLGgDXK1rbCNmxRWFxMPlRrikAUiCzXkIvH6rdo38zMFfF5MtXwc+BtYavVmletl2ixXOyPmtcnWqjD4Ugzd4+Dloo6vZv0tdU+/dvncIpupUHJaQdX0kiwGZTP1F1gSo9i8ghKypD8eW6GI+4gA/NHU72W7Jo9OZ7W4jJ45PvoM3tJ4aPme48pqwL/M6n8xTZHuuERhwDv88932uL/Y+4YtSNXsaR40e+aCDKItLiVThinIR/SbYTxaxrjjutTtk9FE1ChfVvYP/HneKnTBDx20hKRySy2UQau7aI5MBYDonljMX0FwwIuV4dxe+4Cwm9f5fkypg5rFZNiYWEND+OJHGtLYzqq9QvTs9yvc/eqETBMCqi89jLF77mE0IzHhNmK6YnIe4wHL4cQr+PavuYwfnK9q5TypYQq8/WII5/ysOFo/2sNYLcWrG283mBKHRcKfOHgNwhTVRdbFj2JmFV6pk++sW4r49kp6FFYhDl5tj1oCXeHbcj9qCl1OVsWmzqt3TQ9NxKJeDFBJ+U3eAzgol6zyhWy+vsaoIsiuQH5FC5qIHM1bjOooIFdYgbyZZdd5ce+U66EF4hdpxl6k+Vwcvf6ILnzGDcPemVmiYwYM9XRbtURsctukP/5yn1+DA3wqKzBcGCuI8LPSYAVgwTPxVQb396xOTciKDV/mT+9scrF+DDGvDIZXkYjJxkVNt/s5GCcvscgffLCBrDfKaf5diuIZG40YPXx82HtB0aZLekiXglXCU+BQTlvhbzPk8MgBhbyNcfAjPjj84gjfSJxKBGDdG6udhwgtEzYRQJT/3je9XpFxoJkEGcH9Ta2dkuK9drMPaQrNNeIZ/Rcs32gWI8faq8VrGzt+XHglyOQH3MlRMafP88XiNV1IQTs31d+JURCGURiMj3rOzvJeFFL7VAd5BAzCpbKC4CtOlH0E1Gdh3O8Ji7DXxrPSl7GIuc+7wJn7cETNk7cfX2ZEsWzlQ3qHbTrMEojk7va+MpHFEE5cv5InQshZmohoY4L8klt4ET3526ZvnClpNB1+irsL5EubeTCCp/XY9QUWsWRY74uJWb9LvAvtBrXHx2iCEF7+zlC1/0375fgNgW6u3h38hWLnI3rOsnO/3sEx8VXGTb9iImJbaTt4xTr6AHOW53vMiuc3KtKp7hRiDixa/GoxVVkGEmlSatUAKmy9na41sKoZje/QFXBuaw13dD43W4gvpC4zqxuhOjSd/mIwduFZyAcyQILb41BonCHW+aEosRIb54GuI7QQace5SUqmxJDCTMHhfkSuboZyVXHFaUJS566EUTI1wgCGF+/vVSqQYx5g2k1vrqCYI+UsFbS5QcF7QrpdRSKcBYEo+xVnKz4nQEZnqZvLYNRdFWDUXRDQ3xvNKpN3jsvG9+/7E6O8aFdDZWgg+4kQ2RCNsM+Ma1lZoPGAMRwM5jRqeFO0we5MiHg//YRX6Yp/jEfYIbEy+8NU+sQf4hmAUSpSVPS7gBebl9Ul2j8YtBEnUOEpyEEE6Tf/keqB0QHV+Bq0AORXlSyyx24qx5cakU+l7oRrUotlNxPCu3AqXDelJkmLLaI7Y7dfVQaRYVw0oU7aMuNdOabZpGoJDxgqVhszYBDHavRtq1seqE5bMnGW2NT/5Zy1FmKNkNBEoXdrnsLdlcjhGn/MoIGrGB+ZmDAJKLw8gh/5iXkP24KoixQfZGdgGBIqBRZOb6Kw2EjfIxL4RHBprs06faKxA3zbSSa6eDY4uuojiLzzpjlGLOdPJNaIGZWIjL7X3d5Ox7/YNbOg+yM3SiMmuYBiuPZCmsHCsztcF4g6nV0tnViSvaSgdSFZNe0tu0Oy6OxBZyvZm4qsZuKf6RGxaGIxlsvo68HPBcHfPkPWPadaE5eLel4FPryYkAVBlBP5HNCQ9w+oij5JfpQnWPp9Rf1JKNKykNL2Hq+h1+sG2aB73y18WOmsEGIQd7EeVjewlhGOGrais8U/WbFCom5uzzBPimquQcmIWlJB6Leqe2KVMFCAFVzDqaot2LqKR2HcPNC+D8wie+3a5yabe2UqwxTHbYWWZS+6Grd5e8NGoUJQyC//gk33dx9VZ5Z0uVmRrF7H2l0FepPFxmXY6jfYbiYTpcRem/sVPZKuZA8xp/FQRg8IUL3mPq3D0XejgSrxC8vDqouaM39TMExU9r35m7kMYWnCqsOASshCYfvQD/ap5PZsGli3u5GAm9hPcs3vQyCCPvAYT0YZUuuEHp4NnqqMBaLJRBg9SYyTQSYpS9ZGxv6ULSdynZYODEa4bATYuVDgFEuUi7x7cRIWJxVZ0kCQrfFyzG3UIOuVnxCgB06Vhm632xQiaukDfQDg0pz/B6Xa8XdntObOdaXuJZ6PAmP38EkJB/I3zhERQV3Zb9X1Hwlu2aRe00oY4ODk29H8Z2APto0kezBNU7xnvmxruBnTvRCsef7faj+9r0tSbDjE2yY5D7QzcDPMNRlfv+dZkzg9Mv+IMh/KLrOscjH9Zf2lEHbDan0RO8DsiJ9/HnyTFE3voVKSOf0iGdOY48c4zh2ZpcezaAb/BLFRPsGhyfMcYgSJmPFQ3c0Vm6fftybIX3K/z/qGEPNVfrIWGbUsVCtdSUNCvElWCiw8XNWFmiENM/8K8RHR5dO5ZvT6uvmZPKzLTp4gAyUkffhOMF8IpVMaykSfShKMiKpM707sbY0IN6XbMlFEtlwhiVobNUN+DpS41QRxCpb50iz/QG8L4zHWyQ5AVcn40bgiwVEDpeQl/K2hvoc5EVszut4nJd2ki54UPiqLkv16ViCmksK8gYYut4oVc8H6ltrTIt+n+H5lOxIUy6l8QZhvQEoYKb0tiHTrRKxvAJ9x4G3AXwhrO3v5kW1PAMXIWiZXXsED/EXHCjlSjon6PTiEJKHDo1MwVzj1S8Axinry0Rvb5AXoCcENzZwL26YfTcbt9XupE2euA0tS1YDEXmVbeNjrctdun3bJHnHXfJZ4FLwugyjjszoSknZPrHiKdQHhqCQ+HhRbh02Qq+pFoNuoQhfoU9w7giyUmUx5F29qUsKIvPmlMtAEWdkzpYHlUBbLzsKMwQ32a+u7+OCBvqSg4BNsQzC6KHFnB9PjxibTcctXCRr0ahuAq6T3DeGsMM/6OwtcTEth/zTGJEQPYCnafHdc0jwBfq7iA1P5MEJejQCQPotKkiWhNMY1uYe8UN9DLFb7eoj5RuJlcQh+F+WTTtAueKXOLXF2xQvdtpgrFLELbrkf76UGU6d8bLg3FuMbA3I62+UHD9kYweHVwloTn36mYIrJQdirXyw9nylwjiPEPdvATtXYx6uuEnVzBz1VByBxlXNF53EA9eN71WbHnTKOxGcELtEmLp6ALa0zQoO5xpZyzggEjRYtuUARBrF0rKCyAXNHcjUA+6SQBii7VUxt1NwJQWU3RIu7G8UQYaL88nrIK3yKO4Pa1L+TWjA9I1WuUSQJRkNCFlwq4LmA0cT8WrotT9bcTDhAYMmlDb/zFRqJcPzYV4KV2mzdUzjxz3jYkx3R30+YjJXmyAWMhcYSQuYVCJi6mDknoWi5WkL5zn/21fXgeYxfuzYWVRDASMuG0YEqdjUS6i+xITXo32xS9Z3vc/jx+ew01OImp9p6pApUECn7eFdHEg2rOMP4qwa4H8Lmh2yLeTV9J+zYTpfuMlWPcVA0RYEwGA1RN0CplGbdOXMO/wyXej7uuFy19P1RZJ39IFYqJWesFo0XPaLy6XXfR5mXyrM/GhyimX40xpoBPWgS6lKNXKrCIdUBGgBEezYDRHvHlmic5Yrv3KbS+MRbpDwYSA/zX0Er2ZQXptQLwdLcQl1I54T+G/CYg5JfK6aAu2rnsfGa3jHz5+ekDxRvYFyogOwcdQZwoucQlOsFPJCAl9i6f2rWNULVLhNN5tMxYy+vNihEvaaAfbjh23Lr8+dnA/wYHDi5MVKo5RXDWepZbzozGzTCO41RMg8BvAK5RmR/DP+M0l6ztLQCA9n8djOKy7hhnPVz2PXuaGRxe6ioXc2ye+9IygIeXncAfAYQJKkBDb0BKcBB4Hm5eTGuTGBBy4EWlDgrDbBk13FBFJFQt06whPaJoTG/Npum4mdDicxAfx6UFzr13Jf/QUOAzg8ZRqxBU30IIIwz17/lbyFl6/uL+jdy43U4pkhl8BlokQq+6bBLWPcBYiBQqOXG+hwqOOAlQJCtf6CGsgApWELZc4tBdrRhN0HdOZeamSNI7Ur3icRJ4BM0mTNYpgMTyceWVtjfjfnwp+to0ky0tKbfw9dxyb2vwcmJj/A+dJ9+A7485DjrjDmGGoZApn9havqObrWmT/WwwXsY9/8UtZmlubwo68mOuok9u0kfd+qH9qgW3NIST8nOFzpX5mQenqwP5OBHdxhLpXQAi7Gce6L56oxC5uWgicnRyl6GBXWGQy2VX/de8v9n9ZmejhjBguNEL4nQkvQISegMrhMEqS/vEeAnA9xreiPzBylt9+NliseaMMOInGfTDY0i0IsH1K6lyA5y2eBrk5Zuwyv/eDJQdJJKYDrdyw9pKq2mtarR7FDjhHuJRA5MMPncDXgRBJNTt6sfa0SC+VI9/shOQj760uj0uutSGaaACHUsr9BRfreSjWaxyMaTlKTCAx0r5936OTGz/mkUO5G4076RLpC91YHw82KiIFRZgaWPOyIWrXdNdatFcdl5WoIZxpUTXHmcbkzxFh81+V9Ikjx1Svejn8jQLtsTy5IMvsxulpPi//gZKFlrqv/rf+ztO/6B0v8/Sk1di2pK4xVHiP4Fnl2bodRWvdS9dl42ChAkULNVxWXSTrTQyuXi6lUsfX5Vo4nW0J9uH4BjZ7eX85XWdW/7pWnJu0XRZZSHSf0DdIkGLU7CfVTIWtWRSwpXle12ywXJlIJiY/kadNa61HBtZDMaFP6A1gWSM05LoBFsneLxxXqM2LiYTN8CTor5Anql+CUdzemI6GkK1mzpVrc8XJkYi+55Y4iBAfE25w6pHWp09yhmczRvmtLtfC6K0m8721ITCq26B7Wv3EAIEz7xPonSN8DdwJNzqBLt87X/lQCjOALCKu2x516M/n2YxOGJu3C/aube2vO3th3mjIB0J5SnYGYtGhZy/fsR3yLKDourBlEH8TXF0Vr4Vh+y4UEsABrhZv9GznYAzv9JiABOBtWrfsbhSgKoA4MZHOW67DCs4spPn6Uf9Rt4z0T0YzR9M7jVpx0DfIgQiwREZq4vdd4PIUXAlEDe5skb3ya61pzSZ73pXOWVsKfxRuxytH9LXe9BJkG5WNmU62+tFNQsMcvMvwiml7JGWX894JNPV8iAiDngTiM/dRaRgGLlMU39hksV1Z50Z92uVCg4ZYjXHr3+ZQv6gfQEScD3aPUI034Kg65VvbVGI0FjtBcXFSjoHr1U/UPHZVlb1rmhoGDa+/X7ZRwAvs0+h6pRufaOZSEsvZ1GogZOQ/vpzC4VqkLxxLUeXFcCZKr+Um1M89QtrpmLmVczhgN9R7NxGB1uAIE4zDn0Z4bMDxi0xHLPsHmYLcnGK1enWA/x6rCkgjYteg1w3cKdAo8rEPbvMgi6G2LpNNu8t0ptRP/JnjXT1lS1P36tgFJntxAPyqaQ4Q6OFYLDbACBHJI/Ca6ojp7+K3GnVzoQF9+tl9LH2xs78+fgcSOUOUg+GYRZPFHRAPw5RKR8V+ksesn9k4TZ8e2QfpEthjsuA7hTUx/gH+IKKQk6tzUozlAgd1SJ+d+gCPK0gAi+BmrKvSe3XNhRO9xdWX+Ia52N4kJl0ur9YsWz1CV59wYsrtxyMhch5ijYrwgwd6IRvUdT3ZddKz267Wxw79y6BZR9sgOvjQmVJ2eiGqYCUjcG/JiwbiTmhDu8VHx5WJRrxRbM6dV46AZIcZNH7KlZtL9+lzOWL/IC/GrHD1JgDXyUIwpGVBFm4iYehr24JFq7DF70GskcpfLKxLVu8k3HBlBPGNZ/ts1SGhYbPn1hHtxGfAMp9PgMk9x7pK+tS4tDVMc/WHL4Ish1IOFsOvEyyWp2UZ/cWATTQDARnRdAFSlNFuqzOabsw3TkwQiJkHwChKSuCT9F1/uDCtVWDz+SBx8+N3Xeyb0K5GzJMY1roSNPztk+olNLwnebcU59uHT9MIqcZnDBR8vPb7majFj51axPYtJ4ok7Bdx5UGEHXnFo3es17nFbobswhmumsytolIa1ONesTVJrRSy1AAPBlkkqli6drKc6lcVBxtp4AtE7J2qqIrjvJA5wA+4LGGEKeAmr8xRlHUwG9tonbj2hwptZgIOrevpMUCmyBFL1GLfLrqME4i+FPrYvAzCEL90AensvAYnBMnXTHXwvNs1e4Q3fb/EUMdvJSizo36JGdMRxsvB1+Zoil1/V0/reoqDWe5quFBe7a/ATCXMkiQ36p8zbjywokhvGzqdqDdkOK1Pe9mYJcovADoqWWR01I2mXPNvQJAsxSbB12XGksb1j4czHsHbkkJo7n9/QYJcq317aeko4QXc8X3PLU9CWzX8HL+RBm2tFHfkWIeStQjw6FAyZbaP36ucXSO1L3oeW/UFWYlSePXfb4rd2DOW0fRSQLC3AxAf2GuKtQm3TZgJheOyUUcDIGoLpcE9v1mufPCnHLG8RX5GMMh7GRqAOMc+WRYWdnGZo4tGCWHsDSmeBPZAprHSi69389iI+A87BUPf2MXAHA8UfVwOPDEC5BDqhfyG34ZLBUBGXSzCmdCbwIER6LMmxIXs91CyNt1fcN4QKnycSmO59f5lpU2LfbksgYFeiJj/w9n1sM1eRGjDMUMNsRrt/CwytdWcEBFNhN9wGWhSfK81izGRmkmhdGLI0a8dRDv/rOuEvSerQSP6Tq0PfABLnkGpH+4Fm9Aw8YdlXiISXThfFc74wbqX0bNr5c/sMhulR2GIZM9lAA+tzheItzc+l/HX6MOA+/dkFQYufx2xtYSRqoHGR2JP4sZhKea9/TND6Q2aeUSNBV52hQqGaR1DoyD4bxTsTwYWPdE1BbQDbtLthB/EieJM+49YkH0vwELRrxaZ1PLi+jQ58qrSxHBQYAeWB6frLsf0oGBTRRUXeXMtIfPq1UbQAeOgsVbpwFMBSMY66G9Os4s1tdQfmk/bgHEeUaa1M1VRVmcDU1h7ARwM6fBiZaCg/Rc9JUtXwlmFKVBhzqA3ftVlI+luxMbcEA2nnXduLggG2Jg+U7yddaFuMPgdZzmt1mHaxk2P9foKEN6qwEFJu9fxSQ5LrtFFAFArADHhmd66DPiidzs6i6bUY0lI6/uK3HF5OEHYQBCMimORA1pODbnMGnC9vAOCwGSmJkggjmCm2FC379cSmVH9wU3P4MuJntNurOKkytVRRd/9yTPePLl9EnymgK/zd/ePKkR1k2/b/9PNdWZjH4b8LNcKIEA54wnYkH3JgLwKNwJ2JAiLpbti6POKGAs+M9XxvZQekm0hBEPyXuoGZ6bK5GNeGuJCrrKrMHwxiEvru4ykyb/fjDSENfKtJFiqWiCi0xV0Ypt/RSve7nGQ9kuER/bqV1J8tW5I/I653zpBqz2AaEw8maRAvcMkf8ILrgXiSezg4hOkA/FmEJYQwK/qZq2JYEHyeKHOarUk3Ecgafkatvk/iLUIg/ECJ6wO7FPQUQeMzVisHIESDYv0H8vEg5e0wFGm9JeW0lSVY/lCha0bFuVhdXtyv6BtU4D/5EugiiUNxwxjiW1U8xRfy/HKRBg6Qhk0FNvj+Fy3JDYLVy7jXgcqpv/7WTbpxixqDjnYvPfGC/UwxXTKXRLYASoo6vCzb4oh33KwDyLfmKSLy70sHw2W/wIlPZnoG3dM7oAZVvEgWlFR4lBoC5EgeGeqT5KLk+TmTVaTH3YixVpn0ck9fjDYJMsqRs7z6XkPD72yPJohK0+nC2PujkUjqlGNYw8x16hkylsLySBjKvqcfE6EynWWjiDLPOusvgtJMTSHjPWCf8MxLW/DUZDjM00qKbwWP/NCGmdnMVVJmrk+NmmvXO/WqEq0jR1LkUJQfs0gdL2csEW5ejweT+/U6p3CWhRVkV0KUVRCmWfSZPIiG2k2BI18+vPzEGHhAvQ/Irw5tM08QABg3okXQFOKkzW1bnv24/E9tRNNku+u4YDKGatc3xacxwB2EKxNrL//8PYeG155h2ea7Aph2GRN+AopMBbovEkuc5ZMYLYmfQbozoyiBXYggetvjr0FQL4eGr4V62621btX1g0Zp+jAFsT9sUDbyFM4MBPQo8R0RM2IIyhkwtZ6aKoGRQ9shgNHJ6MyrWDgNAbDOWibIOrh/z2YeY9gjtuWzSWC+KQ/KojduMFBnqt0V3BCUCfaJsYW1S1MtJ8xmw7j55+LRKy+RCZjH/xmTMoIw0+He6PLTGWlqVDAV8FcC69Hae9DRJC7SwIHijnPp1yCyoEdrf63OjguyDHZcf9S9w2vt21BVGBDl9cEkDhgY48YAAJQeL2WiqK0aju1whbTDsVTmyn7uHN8X+q9kJBFlqBPpBm9e0fxrdNrMxtiNxF9R//mmVRX+chfxwqRvav1tU8M/58o6QpPaxVxdmJCya3Oqj1BG054SuheDiVkbIxIb1nKg82DUdooErhmcnz+zcW1sJ51oUAAAh8WJwX1BLjhSuqUNuE6tJyAj2cAAAAAA==";
        base64ToFile(base64, "a", "d:/");

        for (int i = 0; i < 5; i++) {
            logToFile(new File("d:/a.txt"), String.valueOf(Math.random()));
        }
    }

    /**
     * base64转为图片
     *
     * @param base64
     * @param fileName
     * @param filePath
     */
    public static void base64ToFile(String base64, String fileName, String filePath) {
        if (base64 == null || "".equals(base64.trim())) throw new RuntimeException("base64 param is empty!");
        //创建文件目录
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        if (base64.contains("base64")) {
            Matcher matcher = RegExpUtils.base64Reg.matcher(base64);
            if (matcher.find()) {
                base64 = matcher.group(2);
                fileName += "." + matcher.group(1);
            } else {
                throw new RuntimeException("not valid base64!");
            }
        }
        File file = new File(filePath + fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] bytes = Base64.getDecoder().decode(base64);
//            Files.write(file.toPath(), bytes);
            bos.write(bytes);
        } catch (Exception e) {
            log.error("convert base64 to file error!", e);
        }
    }

    public static void logToFile(File logFile, String msg) {
        Calendar now = Calendar.getInstance();
        String t = String.format("[%d-%02d-%02d %02d:%02d:%02d.%03d] %s", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1,
                now.get(Calendar.DATE), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND),
                now.get(Calendar.MILLISECOND), msg);
        try {
            Files.write(logFile.toPath(), Collections.singleton(t), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("log to file error", e);
        }
    }

    public static void a() {
        try {
            InputStream inputStream = new ClassPathResource("lua/incrLimitExp.lua").getInputStream();
            // 从classpath中加载资源，必须以/开头
            InputStream resourceAsStream = FileUtils.class.getResourceAsStream("/lua/lock.lua");
            // 从classpath中加载资源，必须不能以/开头
            InputStream resourceAsStream1 = FileUtils.class.getClassLoader().getResourceAsStream("lua/lock.lua");

            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources("classpath:/lua/lock.lua");
//            File file = ResourceUtils.getFile("classpath:/lua/lock.lua");
//            log.info("file: {}", file.toString());
        } catch (Exception e) {
            log.error("err", e);
        }
    }

    public String getVersion(String path) {
//        classpath:/static
//        classpath:/public
//        classpath:/resources
//        classpath:/META-INF/resources
        String root = ClassUtils.getDefaultClassLoader().getResource(StringUtils.EMPTY).toString();
        if (root.startsWith("file:/")) {
            root = root.substring(6);
        }
        File file = Paths.get(root, "/static", path).toFile();
        if (file.exists()) {
            long version = file.lastModified();
            path += "?_v=" + version;
        }
        System.out.println("version:" + path);
        return path;
    }
}
