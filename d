[33mcommit 0d63c5d7688952a6dc24aff96c68db2db949c059[m[33m ([m[1;36mHEAD -> [m[1;32mfeature/pti_integration[m[33m, [m[1;31morigin/feature/pti_integration[m[33m)[m
Author: Hoang Vu <hoang1791997@gmail.com>
Date:   Fri Aug 9 15:11:07 2019 +0700

    completed request to pti's api, add pti's dto

[33mcommit cc7d53680314aa6374396cb13dbd1a77c1c5f03e[m
Author: necki <necki.rismawanto@gmail.com>
Date:   Tue Jul 16 15:11:09 2019 +0700

    Add user b2b transaction list in apigateway

[33mcommit 4a8652ff46e86eb9a06acb01b28bc6c246088371[m
Author: Dave <dapitlast@gitlab.com>
Date:   Fri Jul 12 15:41:57 2019 +0700

    Update properties naming

[33mcommit d741564a1bdbec3c2d4fdfdad110da9a7c7d9ad8[m
Author: necki <necki.rismawanto@gmail.com>
Date:   Fri Jul 12 15:18:34 2019 +0700

    update select *  to select column name

[33mcommit 82add95436a76e5fb92a1670a8d6f6b95fa9057f[m
Author: necki <necki.rismawanto@gmail.com>
Date:   Wed Jul 10 16:50:35 2019 +0700

    Merge api-gateway batch to master

[33mcommit e8e9aae2a78c9e0d5f1cfee705a284de9960d43d[m[33m ([m[1;33mtag: v4.0.0[m[33m, [m[1;31morigin/feature/vietnam[m[33m, [m[1;31morigin/develope[m[33m)[m
Author: Dave <dapitlast@gitlab.com>
Date:   Thu Jul 4 14:06:09 2019 +0700

    Bump version for master release

[33mcommit 5b9cb04312babc92b7796011291acdbedc969205[m
Author: Dave <dapitlast@gitlab.com>
Date:   Thu Jul 4 14:01:08 2019 +0700

    Disable queue processor at api-gtway

[33mcommit aa90599d1e552e43db63b74902c5a8e5d9dc9739[m
Author: Dave <dapitlast@gmail.com>
Date:   Wed Jul 3 23:17:31 2019 +0700

    Add hardcoded quick fix to parse id_ID as in_ID

[33mcommit 436a3d64b24f5c404cff9f5ba77e0ff3d6e83d01[m
Author: Dave <dapitlast@gitlab.com>
Date:   Fri Jun 28 15:33:21 2019 +0700

    Fix product mapper

[33mcommit b5bf8796a2a7ee47daf20696b0a40974b091a835[m
Author: Dave <dapitlast@gitlab.com>
Date:   Fri Jun 28 15:18:03 2019 +0700

    Fix coverage_option translation

[33mcommit 13e35c4a58907bcf617ac8f69c116c7221dd8639[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 24 18:45:14 2019 +0700

    Add health-check for aws elb

[33mcommit 45a858e71ff840437beda109d3961f43f6e6040e[m
Author: Dave <dapitlast@gitlab.com>
Date:   Wed Jun 19 17:08:52 2019 +0700

    Change error message

[33mcommit 4f09e55adf71c76a0a5f32f0d8d923e31354b3e6[m
Author: Dave <dapitlast@gitlab.com>
Date:   Wed Jun 19 14:30:22 2019 +0700

    Remove period from pti pdf template

[33mcommit c2bb82a27d8e33014d48f796c88348a7b7930cf0[m
Author: Dave <dapitlast@gitlab.com>
Date:   Wed Jun 19 13:47:29 2019 +0700

    Fix english toc

[33mcommit c0ee4d99e27fa92082d16b328824ac68685653c7[m
Author: Dave <dapitlast@gitlab.com>
Date:   Wed Jun 19 11:49:14 2019 +0700

    Fix request logging for payment by using request-body

[33mcommit e9c51e436ed39448430ef328e0ba270518486b56[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 17 18:19:45 2019 +0700

    Fix null pointer exception

[33mcommit 4b2977b1259da0453533dfb1b564d8a176641b59[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 17 15:47:05 2019 +0700

    Fix free-insurance/invite translation
    
    * Remove invite-voucher-dto cache
    * Retrieve voucher-id in sql
    * Add translation for voucher model-mapper

[33mcommit 1aa10099abf257f5214599b580fbd0e82f92118e[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 17 15:44:01 2019 +0700

    Add more log for payment controller

[33mcommit 93cf0198948172d97a32d00f143d3c4ead95a36d[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 15 11:55:56 2019 +0700

    Change 2c2p payment to set payment status to charge on first insert

[33mcommit a4634e635f0f29bbfa7c471fa0f2bf814389dbca[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 15 11:42:40 2019 +0700

    Change older insurer module class to deprecated

[33mcommit c3ef3ef4c65beaffd3c7a2059943871afa3db962[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 15 11:12:13 2019 +0700

    Add insert payment charge log to payemnt 2c2p

[33mcommit a015517ee51242b246c12f59e8ab6cbfef91df7c[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 15 10:33:58 2019 +0700

    Modify payment-controller to throw exception if order not in submmitted

[33mcommit 519c0b0e159a58b408340e1e9305c498f2667985[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 15 10:25:55 2019 +0700

    Modify download controller to add pdf extension to policy

[33mcommit 21bc70c500d5ca5ad7961438620dad58c4657bbc[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 15 10:25:28 2019 +0700

    Change 2c2p order-id-map generation to sequence

[33mcommit 9fbac31c22134f0f96f2f9404a4302dcd4489f98[m
Author: Dave <dapitlast@gitlab.com>
Date:   Fri Jun 14 10:11:17 2019 +0700

    Change 2c2p-payment-controller config to ninelives-config

[33mcommit 45588060879f14449e2c9d37958c482a88f00e0c[m
Author: Dave <dapitlast@gitlab.com>
Date:   Tue Jun 11 16:48:41 2019 +0700

    Update vietname translation

[33mcommit bf8c273780eb9565d29714a7a447e05d40799b5f[m
Author: Dave <dapitlast@gitlab.com>
Date:   Tue Jun 11 16:30:53 2019 +0700

    Fix rp and vnd display issue by separating error properties

[33mcommit ececb912e7c88099e980a903d73b09727c69b484[m
Author: Dave <dapitlast@gitlab.com>
Date:   Tue Jun 11 16:29:51 2019 +0700

    Add debug request filter to payment module

[33mcommit 6fb9b7e6060483488b518862f17472d38e074f10[m
Author: Dave <dapitlast@gitlab.com>
Date:   Tue Jun 11 11:05:37 2019 +0700

    Change controller name for midtrans/2c2p

[33mcommit 68ef3aade1ec691d232335880fbf65e8d1c545e5[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 10 18:29:58 2019 +0700

    Update translation for privacy for vietnam

[33mcommit 57874ef6fd7143df524a36c13d68653932e9e300[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 10 18:12:04 2019 +0700

    Update translation for toc for country vietname

[33mcommit 70cf4d5e34d4df2acbfd04710375b80a4e01955d[m
Author: Dave <dapitlast@gitlab.com>
Date:   Mon Jun 10 16:53:02 2019 +0700

    Update translation for country indonesia

[33mcommit 22ec9b0ccce79219e98acd21562f596ef48ed35c[m
Author: Dave <dapitlast@gitlab.com>
Date:   Sat Jun 1 22:33:35 2019 +0700

    Change fetch order, rollback to use pre-existing SQL
    
    * Rollback fetch order
    * Remove duplicate sql method
    * Remove 'new' sql and use old one instead

[33mcommit c5a88a2e920dadc1733f073dfb126255eeed7e4e[m
Author: Dave <dapitlast@gmail.com>
Date:   Sat Jun 1 08:52:15 2019 +0700

    Change claim to allow empty bank code for Vietname case

[33mcommit 64f258a3c90a5a3755b510cddb46f1d06f4dc69e[m
Author: Dave <dapitlast@gmail.com>
Date:   Thu May 30 22:45:58 2019 +0700

    Add i18n support for batch push notification message

[33mcommit da4e7dde9c35e6120b039a74b53da39d5ceeed3b[m
Author: Dave <dapitlast@gmail.com>
Date:   Thu May 30 17:08:21 2019 +0700

    Remove birthplace restriction from user update

[33mcommit ea8186c457fc5cca92b5d209e412fb7a3efac590[m
Author: Dave <dapitlast@gmail.com>
Date:   Thu May 30 14:21:34 2019 +0700

    Update cache

[33mcommit 0fe963741ff154a91768f3d7493acf027c6baea9[m
Author: Dave <dapitlast@gmail.com>
Date:   Thu May 30 14:05:12 2019 +0700

    Fix test null pointer caused by new translation service

[33mcommit 17b82a48c6e4197e1d693680aadc37f0198982e2[m
Author: Dave <dapitlast@gitlab.com>
Date:   Wed May 29 16:47:03 2019 +0700

    Refactor translation to use multiple translate table

[33mcommit f70c33a88ec0fbff660967436518df2497f2bae3[m
Merge: 9f2f82d 0a6ca5b
Author: Dave <dapitlast@gitlab.com>
Date:   Fri May 24 14:54:53 2019 +0700

    Merge branch 'develope' into feature/vietnam

[33mcommit 0a6ca5b7a2ff5c7f18a008b2a0d55e802e7cbdb3[m[33m ([m[1;33mtag: v3.1.3[m[33m, [m[1;31morigin/hotfix/ktp_image_and_no[m[33m)[m
Author: Dave <dapitlast@gitlab.com>
Date:   Fri May 24 14:49:27 2019 +0700

    Change to allow empty ktp-no for existing user with id-card-file-id

[33mcommit 9f2f82dd65057e1361152843726b4b81071addd6[m
Author: necki <necki.rismawanto@gmail.com>
Date:   Thu May 23 19:52:49 2019 +0700

    update page payment success and failed

[33mcommit 8042e3e46a3e6cf30450