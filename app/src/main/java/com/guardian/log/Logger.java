package com.guardian.log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by JGabrielFreitas on 27/12/15.
 */
@Table(name = "Log")
public class Logger extends Model {

    @Column(name = "PackageName")
    public String packageName;

    @Column(name = "Ticket")
    public String ticket;

    @Column(name = "Content")
    public String content;

    @Column(name = "BundleExtra")
    public String bundleExtra;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBundleExtra(String bundleExtra) {
        this.bundleExtra = bundleExtra;
    }
}
