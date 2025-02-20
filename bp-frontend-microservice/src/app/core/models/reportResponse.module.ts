import { Account } from "./account.module";

export interface ReportResponse {
    startDate: Date;
    endDate: Date;
    accountReports: Account[];
  }